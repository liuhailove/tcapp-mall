package com.tc.mall.portal.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.tc.mall.common.api.CommonPage;
import com.tc.mall.common.exception.Asserts;
import com.tc.mall.common.service.RedisService;
import com.tc.mall.mapper.*;
import com.tc.mall.model.*;
import com.tc.mall.portal.component.CancelOrderSender;
import com.tc.mall.portal.dao.PortalOrderDao;
import com.tc.mall.portal.dao.PortalOrderItemDao;
import com.tc.mall.portal.domain.*;
import com.tc.mall.portal.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前台订单管理Service
 *
 * @author honggang.liu
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 购物车商品服务
     */
    @Resource
    private OmsCartItemService cartItemService;

    /**
     * 会员收货地址服务
     */
    @Resource
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @Resource
    private UmsMemberCouponService memberCouponService;

    /**
     * 积分消费设置
     */
    @Resource
    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
    /**
     * 库存服务
     */
    @Resource
    private PmsSkuStockMapper skuStockMapper;

    /**
     * 订单Mapper
     */
    @Resource
    private OmsOrderMapper orderMapper;
    /**
     * 订单DAO
     */
    @Resource
    private PortalOrderItemDao orderItemDao;

    /**
     * 券历史
     */
    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    /**
     * redis服务
     */
    @Resource
    private RedisService redisService;

    /**
     * key
     */
    @Value("${spring.redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;

    /**
     * db
     */
    @Value("${spring.redis.database}")
    private String REDIS_DATABASE;
    /**
     * 订单Dao
     */
    @Resource
    private PortalOrderDao portalOrderDao;

    /**
     * 订单设置
     */
    @Resource
    private OmsOrderSettingMapper orderSettingMapper;

    /**
     * 订单项
     */
    @Resource
    private OmsOrderItemMapper orderItemMapper;

    /**
     * 取消订单服务
     */
    @Resource
    private CancelOrderSender cancelOrderSender;

    /**
     * 根据用户购物车信息生成确认单信息
     *
     * @param cartIds 购物车商品Ids
     * @return 返回确认信息
     */
    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), cartIds);
        result.setCartPromotionItemList(cartPromotionItemList);
        //获取用户收货地址列表
        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        //获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        //获取用户积分
        result.setMemberIntegration(currentMember.getIntegration());
        //获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);
        //计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    /**
     * 根据提交信息生成订单
     *
     * @param orderParam 订单参数
     * @return
     */
    @Override
    public Map<String, Object> generateOrder(OrderParam orderParam) {
        List<OmsOrderItem> omsOrderItemList = new ArrayList<>();
        // 校验收货地址
        if (orderParam.getMemberReceiveAddressId() == null) {
            Asserts.fail("请选择收货地址!");
        }
        // 获取购物车及优惠信息
        UmsMember currentMember = memberService.getCurrentMember();
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), orderParam.getCartIds());
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            // 生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartPromotionItem.getProductId());
            orderItem.setProductName(cartPromotionItem.getProductName());
            orderItem.setProductPic(cartPromotionItem.getProductPic());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
            orderItem.setProductSn(cartPromotionItem.getProductSn());
            orderItem.setProductPrice(cartPromotionItem.getPrice());
            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
            omsOrderItemList.add(orderItem);
        }
        // 判断购物车中商品是否都有库存
        if (!hasStock(cartPromotionItemList)) {
            Asserts.fail("库存不足，无法下单");
        }
        // 判断是否使用了优惠券
        if (orderParam.getCartIds() == null) {
            // 不用优惠券
            for (OmsOrderItem orderItem : omsOrderItemList) {
                orderItem.setCouponAmount(new BigDecimal(0));
            }
        } else {
            // 使用优惠券
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
            if (couponHistoryDetail == null) {
                Asserts.fail("该优惠券不可用");
            }
            // 对下单商品的优惠券进行处理
            handleCouponAmount(omsOrderItemList, couponHistoryDetail);
        }
        // 判断是否使用积分
        if (orderParam.getUseIntegration() == null || orderParam.getUseIntegration().equals(0)) {
            // 不使用积分
            for (OmsOrderItem omsOrderItem : omsOrderItemList) {
                omsOrderItem.setIntegrationAmount(new BigDecimal(0));
            }
        } else {
            // 使用积分
            BigDecimal totalAmount = calcTotalAmount(omsOrderItemList);
            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, currentMember, orderParam.getCouponId() != null);
            if (integrationAmount.compareTo(new BigDecimal(0)) == 0) {
                Asserts.fail("积分不可用");
            } else {
                // 可用情况下分摊到可用商品中
                for (OmsOrderItem orderItem : omsOrderItemList) {
                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, BigDecimal.ROUND_HALF_EVEN).multiply(integrationAmount);
                    orderItem.setIntegrationAmount(perAmount);
                }
            }
        }
        // 计算order_item的实付金额
        handleRealAmount(omsOrderItemList);
        // 进行库存锁定
        lockStock(cartPromotionItemList);
        // 根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(omsOrderItemList));
        order.setFreightAmount(new BigDecimal(0));
        order.setPromotionAmount(calcPromotionAmount(omsOrderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(omsOrderItemList));
        if (orderParam.getCouponId() == null) {
            order.setCouponAmount(new BigDecimal(0));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(omsOrderItemList));
        }
        if (order.getIntegrationAmount() == null) {
            order.setIntegrationAmount(new BigDecimal(0));
        }
        order.setPayAmount(calcPayAmount(order));
        // 转化为订单信息并插入数据库
        order.setMemberId(currentMember.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        // 支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        // 订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        // 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        // 订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);
        // 收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        // 0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        // 计算赠送积分
        order.setIntegration(calcGifIntegration(omsOrderItemList));
        // 计算赠送成长值
        order.setGrowth(calcGiftGrowth(omsOrderItemList));
        // 生成订单号
        order.setOrderSn(generateOrderSn(order));
        // 设置自动收货天数
        List<OmsOrderSetting> orderSettings = orderSettingMapper.selectByExample(new OmsOrderSettingExample());
        if (!CollectionUtils.isEmpty(orderSettings)) {
            order.setAutoConfirmDay(orderSettings.get(0).getConfirmOvertime());
        }
        // 插入order表和order_item表
        orderMapper.insert(order);
        for (OmsOrderItem orderItem : omsOrderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        orderItemDao.insertList(omsOrderItemList);
        // 如使用优惠券更新优惠券使用状态
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        // 如使用积分需要扣除积分
        if (orderParam.getUseIntegration() != null) {
            order.setUseIntegration(orderParam.getUseIntegration());
            if (currentMember.getIntegration() == null) {
                currentMember.setIntegration(0);
            }
            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
        }
        // 删除购物车中的下单商品
        deleteCartItemList(cartPromotionItemList, currentMember);
        // 发送延迟消息取消订单
        sendDelayMessageCancelOrder(order.getId());
        Map<String, Object> result = new HashMap<>(2);
        result.put("order", order);
        result.put("orderItemList", omsOrderItemList);
        return result;
    }

    /**
     * 支付成功后的回调
     *
     * @param orderId 订单Id
     * @param payType 支付类型
     * @return 支付结果
     */
    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        // 修改订单支付状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(1);
        order.setPaymentTime(new Date());
        order.setPayType(payType);
        orderMapper.updateByPrimaryKeySelective(order);
        // 恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = portalOrderDao.getDetail(orderId);
        return portalOrderDao.updateSkuStock(orderDetail.getOrderItemList());
    }

    /**
     * 自动取消超时订单
     *
     * @return 处理结果
     */
    @Override
    public Integer cancelTimeOutOrder() {
        int count = 0;
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        // 查询超时、未支付的订单及订单详情
        List<OmsOrderDetail> timeOutOrders = portalOrderDao.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
        if (CollectionUtils.isEmpty(timeOutOrders)) {
            return count;
        }
        // 修改订单状态为交易取消
        List<Long> ids = new ArrayList<>();
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            ids.add(timeOutOrder.getId());
        }
        portalOrderDao.updateOrderStatus(ids, 4);
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            // 解除订单商品库存锁定
            portalOrderDao.releaseSkuStockLock(timeOutOrder.getOrderItemList());
            // 修改优惠券使用状态
            updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
            // 返还使用积分
            if (timeOutOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(timeOutOrder.getMemberId());
                memberService.updateIntegration(timeOutOrder.getMemberId(), member.getIntegration() + timeOutOrder.getUseIntegration());
            }
        }
        return timeOutOrders.size();
    }

    /**
     * 取消单个超时订单
     *
     * @param orderId 订单ID
     */
    @Override
    public void cancelOrder(Long orderId) {
        // 查询未付款的取消订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0).andDeleteStatusEqualTo(0);
        List<OmsOrder> cancelOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            return;
        }
        OmsOrder cancelOrder = cancelOrderList.get(0);
        if (cancelOrder != null) {
            // 修改订单状态为取消
            cancelOrder.setStatus(4);
            orderMapper.updateByPrimaryKeySelective(cancelOrder);
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            // 解除订单商品库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                portalOrderDao.releaseSkuStockLock(orderItemList);
            }
            // 修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            // 返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                UmsMember member = memberService.getById(cancelOrder.getMemberId());
                memberService.updateIntegration(cancelOrder.getMemberId(), member.getIntegration() + cancelOrder.getUseIntegration());
            }
        }
    }

    /**
     * 发送延迟消息取消订单
     *
     * @param orderId 订单Id
     */
    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        // 获取订单超时时间
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        // 发送延迟消息
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     */
    @Override
    public void confirmReceiveOrder(Long orderId) {
        UmsMember member = memberService.getCurrentMember();
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);
        if (!member.getId().equals(order.getMemberId())) {
            Asserts.fail("不能确认他人订单！");
        }
        if (order.getStatus() != 2) {
            Asserts.fail("该订单还未发货！");
        }
        order.setStatus(3);
        order.setConfirmStatus(1);
        order.setReceiveTime(new Date());
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 分页获取用户订单
     *
     * @param status   状态
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 用户订单
     */
    @Override
    public CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        if (status == -1) {
            status = null;
        }
        UmsMember member = memberService.getCurrentMember();
        PageHelper.startPage(pageNum, pageSize);
        OmsOrderExample orderExample = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0)
                .andMemberIdEqualTo(member.getId());
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        orderExample.setOrderByClause("create_time desc");
        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        CommonPage<OmsOrder> orderPage = CommonPage.restPage(orderList);
        // 设置分页信息
        CommonPage<OmsOrderDetail> resultPage = new CommonPage<>();
        resultPage.setPageNum(orderPage.getPageNum());
        resultPage.setPageSize(orderPage.getPageSize());
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setTotalPage(orderPage.getTotalPage());
        if (CollectionUtils.isEmpty(orderList)) {
            return resultPage;
        }
        // 设置数据信息
        List<Long> orderIds = orderList.stream().map(OmsOrder::getId).collect(Collectors.toList());
        OmsOrderItemExample omsOrderItemExample = new OmsOrderItemExample();
        omsOrderItemExample.createCriteria().andOrderIdIn(orderIds);
        List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(omsOrderItemExample);
        List<OmsOrderDetail> orderDetailList = new ArrayList<>();
        for (OmsOrder omsOrder : orderList) {
            OmsOrderDetail orderDetail = new OmsOrderDetail();
            BeanUtil.copyProperties(omsOrder, orderDetail);
            List<OmsOrderItem> relatedItemList = orderItemList.stream().filter(item -> item.getOrderId().equals(orderDetail.getId())).collect(Collectors.toList());
            orderDetail.setOrderItemList(relatedItemList);
            orderDetailList.add(orderDetail);
        }
        resultPage.setList(orderDetailList);
        return resultPage;
    }

    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单明细
     */
    @Override
    public OmsOrderDetail detail(Long orderId) {
        OmsOrder omsOrder = orderMapper.selectByPrimaryKey(orderId);
        OmsOrderItemExample example = new OmsOrderItemExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(example);
        OmsOrderDetail orderDetail = new OmsOrderDetail();
        BeanUtil.copyProperties(omsOrder, orderDetail);
        orderDetail.setOrderItemList(orderItemList);
        return orderDetail;
    }

    /**
     * 用户根据订单ID删除订单
     *
     * @param orderId 订单ID
     */
    @Override
    public void deleteOrder(Long orderId) {
        UmsMember member = memberService.getCurrentMember();
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);
        if (!member.getId().equals(order.getMemberId())) {
            Asserts.fail("不能删除他人订单！");
        }
        if (order.getStatus() == 3 || order.getStatus() == 4) {
            order.setDeleteStatus(1);
            orderMapper.updateByPrimaryKey(order);
        } else {
            Asserts.fail("只能删除已完成或已关闭的订单！");
        }
    }

    /**
     * 根据orderSn来实现的支付成功逻辑
     *
     * @param orderSn 订单SN
     * @param payType 支付类型
     */
    @Override
    public void paySuccessByOrderSn(String orderSn, Integer payType) {
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria()
                .andOrderSnEqualTo(orderSn)
                .andStatusEqualTo(0)
                .andDeleteStatusEqualTo(0);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(orderList)) {
            OmsOrder order = orderList.get(0);
            paySuccess(order.getId(), payType);
        }
    }


    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ORDER_ID + date;
        Long increment = redisService.incr(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        cartItemService.delete(currentMember.getId(), ids);
    }

    /**
     * 计算该订单赠送的成长值
     */
    private Integer calcGiftGrowth(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum = sum + orderItem.getGiftGrowth() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 计算该订单赠送的积分
     */
    private Integer calcGifIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum += orderItem.getGiftIntegration() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 将优惠券信息更改为指定状态
     *
     * @param couponId  优惠券id
     * @param memberId  会员id
     * @param useStatus 0->未使用；1->已使用
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) {
            return;
        }
        //查询第一张优惠券
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        example.createCriteria().andMemberIdEqualTo(memberId)
                .andCouponIdEqualTo(couponId).andUseStatusEqualTo(useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(couponHistoryList)) {
            SmsCouponHistory couponHistory = couponHistoryList.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryMapper.updateByPrimaryKeySelective(couponHistory);
        }
    }

    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            // 原价-促销优惠-优惠券抵扣-积分抵扣
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    /**
     * 获取订单促销信息
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName());
            sb.append(";");
        }
        String result = sb.toString();
        if (result.endsWith(";")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 计算订单应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        // 总金额+运费-促销优惠-优惠券优惠-积分抵扣
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
        return payAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算订单活动优惠
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }

    /**
     * 获取可用积分抵扣金额
     *
     * @param useIntegration 使用的积分数量
     * @param totalAmount    订单总金额
     * @param currentMember  使用的用户
     * @param hasCoupon      是否已经使用优惠券
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal(0);
        // 判断用户是否有这么多积分
        if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
            return zeroAmount;
        }
        // 根据积分使用规则判断是否可用
        // 是否可与优惠券共用
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            // 不可与优惠券共用
            return zeroAmount;
        }
        // 是否达到最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }
        // 是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration).divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    /**
     * 对优惠券优惠进行处理
     *
     * @param orderItemList       order_item列表
     * @param couponHistoryDetail 可用优惠券详情
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            // 全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            // 指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            // 指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对每个下单商品进行优惠券金额分摊的计算
     *
     * @param orderItemList 可用优惠券的下单商品商品
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            // (商品价格 / 可用商品总价) * 优惠券面额
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, BigDecimal.ROUND_HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * 获取与优惠券有关系的下单商品
     *
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList       下单商品
     * @param type                使用关系类型：0->相关分类；1->指定商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                categoryIdList.add(productCategoryRelation.getProductCategoryId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (categoryIdList.contains(orderItem.getProductCategoryId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                productIdList.add(productRelation.getProductId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (productIdList.contains(orderItem.getProductId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return result;
    }

    /**
     * 获取该用户可以使用的优惠券
     *
     * @param cartPromotionItemList 购物车优惠列表
     * @param couponId              使用优惠券id
     */
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberCouponService.listCart(cartPromotionItemList, 1);
        for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList) {
            if (couponHistoryDetail.getCoupon().getId().equals(couponId)) {
                return couponHistoryDetail;
            }
        }
        return null;
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * 锁定下单商品的所有库存
     */
    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartPromotionItem.getProductSkuId());
            skuStock.setLockStock(skuStock.getLockStock() + cartPromotionItem.getQuantity());
            skuStockMapper.updateByPrimaryKeySelective(skuStock);
        }
    }

    /**
     * 判断下单商品是否都有库存
     */
    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            // 判断真实库存是否为空
            // 判断真实库存是否小于0
            // 判断真实库存是否小于下单的数量
            if (cartPromotionItem.getRealStock() == null
                    || cartPromotionItem.getRealStock() <= 0
                    || cartPromotionItem.getRealStock() < cartPromotionItem.getQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算购物车中商品的价格
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }
}
