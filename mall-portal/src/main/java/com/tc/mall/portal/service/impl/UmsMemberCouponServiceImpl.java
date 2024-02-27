package com.tc.mall.portal.service.impl;

import com.tc.mall.common.exception.Asserts;
import com.tc.mall.mapper.*;
import com.tc.mall.model.*;
import com.tc.mall.portal.dao.SmsCouponHistoryDao;
import com.tc.mall.portal.domain.CartPromotionItem;
import com.tc.mall.portal.domain.SmsCouponHistoryDetail;
import com.tc.mall.portal.service.UmsMemberCouponService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 会员优惠券管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 券mapper
     */
    @Resource
    private SmsCouponMapper couponMapper;

    /**
     * 券历史
     */
    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    /**
     * 券历史Dao
     */
    @Resource
    private SmsCouponHistoryDao couponHistoryDao;

    /**
     * 券商品关系
     */
    @Resource
    private SmsCouponProductRelationMapper couponProductRelationMapper;

    /**
     * 券商品类目关系Mapper
     */
    @Resource
    private SmsCouponProductCategoryRelationMapper smsCouponProductCategoryRelationMapper;

    /**
     * 商品mapper
     */
    @Resource
    private PmsProductMapper productMapper;


    /**
     * 会员添加优惠券
     *
     * @param couponId 券ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        // 获取优惠券信息，判断数量
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if (coupon == null) {
            Asserts.fail("优惠券不存在");
        }
        if (coupon.getCount() <= 0) {
            Asserts.fail("优惠券已经领完了");
        }
        Date now = new Date();
        if (now.before(coupon.getEnableTime())) {
            Asserts.fail("优惠券还没到领取时间");
        }
        // 判断用户领取的优惠券数量是否超过限制
        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        couponHistoryExample.createCriteria().andCouponIdEqualTo(couponId).andMemberIdEqualTo(currentMember.getId());
        long count = couponHistoryMapper.countByExample(couponHistoryExample);
        if (count >= coupon.getCount()) {
            Asserts.fail("您已经领取过该优惠券");
        }
        // 生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        // 主动领取
        couponHistory.setGetType(1);
        // 未使用
        couponHistory.setUseStatus(0);
        // 修改优惠券表的数量、领取数量
        coupon.setCount(coupon.getCount() - 1);
        coupon.setReceiveCount(coupon.getReceiveCount() == null ? 1 : coupon.getReceiveCount() + 1);
        couponMapper.updateByPrimaryKey(coupon);
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = Long.toString(currentTimeMillis);
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length() - 4));
        }
        return sb.toString();
    }

    /**
     * 获取优惠券历史列表
     *
     * @param useStatus 使用状态
     * @return 优惠券历史列表
     */
    @Override
    public List<SmsCouponHistory> listHistory(Integer useStatus) {
        UmsMember currentMember = memberService.getCurrentMember();
        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        SmsCouponHistoryExample.Criteria criteria = couponHistoryExample.createCriteria();
        criteria.andMemberIdEqualTo(currentMember.getId());
        if (useStatus != null) {
            criteria.andUseStatusEqualTo(useStatus);
        }
        return couponHistoryMapper.selectByExample(couponHistoryExample);
    }

    /**
     * 根据购物车信息获取可用优惠券
     *
     * @param cartItemList 购物车列表
     * @param type         类型
     * @return 优惠券历史列表
     */
    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        UmsMember currentMember = memberService.getCurrentMember();
        Date now = new Date();
        // 获取该用户所有优惠券
        List<SmsCouponHistoryDetail> allList = couponHistoryDao.getDetailList(currentMember.getId());
        // 根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            // 使用门槛
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if (useType.equals(0)) {
                // 0->全场通用
                // 判断是否满足优惠起点
                // 计算购物车商品的总价
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.before(endTime) && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                // 1->指定分类
                // 计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartItemList, productCategoryIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                // 2->指定商品
                // 计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList, productIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if (type.equals(1)) {
            return enableList;
        } else {
            return disableList;
        }
    }

    /**
     * 获取商品Id的优惠券
     *
     * @param productId 商品ID
     * @return 券列表
     */
    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        List<Long> allCouponIds = new ArrayList<>();
        // 获取指定商品优惠券
        SmsCouponProductRelationExample cprExample = new SmsCouponProductRelationExample();
        cprExample.createCriteria().andProductIdEqualTo(productId);
        List<SmsCouponProductRelation> cprList = couponProductRelationMapper.selectByExample(cprExample);
        if (!CollectionUtils.isEmpty(cprList)) {
            List<Long> couponIds = cprList.stream().map(SmsCouponProductRelation::getCouponId).collect(Collectors.toList());
            allCouponIds.addAll(couponIds);
        }
        // 获取指定分类优惠券
        PmsProduct product = productMapper.selectByPrimaryKey(productId);
        SmsCouponProductCategoryRelationExample cpcrExample=new SmsCouponProductCategoryRelationExample();
        cpcrExample.createCriteria().andProductCategoryIdEqualTo(product.getProductCategoryId());
        List<SmsCouponProductCategoryRelation> cpcrList=smsCouponProductCategoryRelationMapper.selectByExample(cpcrExample);
        if(!CollectionUtils.isEmpty(cpcrList)){
            List<Long> couponIds=cpcrList.stream().map(SmsCouponProductCategoryRelation::getCouponId).collect(Collectors.toList());
            allCouponIds.addAll(couponIds);
        }
        // 所有优惠券
        SmsCouponExample couponExample=new SmsCouponExample();
        couponExample.createCriteria().andEndTimeGreaterThan(new Date())
                .andStartTimeLessThan(new Date())
                .andUseTypeNotEqualTo(0);
        if(!CollectionUtils.isEmpty(allCouponIds)){
            couponExample.or(couponExample.createCriteria()
                    .andEndTimeGreaterThan(new Date())
                    .andStartTimeLessThan(new Date())
                    .andUseTypeNotEqualTo(0)
                    .andIdIn(allCouponIds));
        }

        return couponMapper.selectByExample(couponExample);
    }

    /**
     * 根据使用状态获取优惠券列表
     *
     * @param useStatus 使用状态
     * @return 优惠券列表
     */
    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        UmsMember member = memberService.getCurrentMember();
        return couponHistoryDao.getCouponList(member.getId(), useStatus);
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartPromotionItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartPromotionItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartPromotionItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartPromotionItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartPromotionItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartPromotionItemList) {
            if (productIds.contains(item.getProductId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }
}
