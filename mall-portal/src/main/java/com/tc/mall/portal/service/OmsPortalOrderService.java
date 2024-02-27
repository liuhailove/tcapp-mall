package com.tc.mall.portal.service;

import com.tc.mall.common.api.CommonPage;
import com.tc.mall.portal.domain.ConfirmOrderResult;
import com.tc.mall.portal.domain.OmsOrderDetail;
import com.tc.mall.portal.domain.OrderParam;

import java.util.List;
import java.util.Map;

/**
 * 前台订单管理
 *
 * @author honggang.liu
 */
public interface OmsPortalOrderService {
    /**
     * 根据用户购物车信息生成确认单信息
     *
     * @param cartIds 购物车商品Ids
     * @return 返回确认信息
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

    /**
     * 根据提交信息生成订单
     *
     * @param orderParam 订单参数
     * @return
     */
    Map<String, Object> generateOrder(OrderParam orderParam);

    /**
     * 支付成功后的回调
     *
     * @param orderId 订单Id
     * @param payType 支付类型
     * @return 支付结果
     */
    Integer paySuccess(Long orderId, Integer payType);

    /**
     * 自动取消超时订单
     *
     * @return 处理结果
     */
    Integer cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     *
     * @param orderId 订单ID
     */
    void cancelOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     *
     * @param orderId 订单Id
     */
    void sendDelayMessageCancelOrder(Long orderId);

    /**
     * 确认收货
     *
     * @param orderId 订单ID
     */
    void confirmReceiveOrder(Long orderId);

    /**
     * 分页获取用户订单
     *
     * @param status   状态
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 用户订单
     */
    CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize);

    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId 订单ID
     * @return 订单明细
     */
    OmsOrderDetail detail(Long orderId);

    /**
     * 用户根据订单ID删除订单
     *
     * @param orderId 订单ID
     */
    void deleteOrder(Long orderId);

    /**
     * 根据orderSn来实现的支付成功逻辑
     *
     * @param orderSn 订单SN
     * @param payType 支付类型
     */
    void paySuccessByOrderSn(String orderSn, Integer payType);
}
