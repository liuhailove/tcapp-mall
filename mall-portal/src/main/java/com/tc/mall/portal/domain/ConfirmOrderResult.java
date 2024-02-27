package com.tc.mall.portal.domain;

import com.tc.mall.model.UmsIntegrationConsumeSetting;
import com.tc.mall.model.UmsMemberReceiveAddress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 确认单信息封装
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class ConfirmOrderResult {
    /**
     * 包含优惠信息的购物车信息
     */
    @ApiModelProperty("包含优惠信息的购物车信息")
    private List<CartPromotionItem> cartPromotionItemList;
    /**
     * 用户收货地址列表
     */
    @ApiModelProperty("用户收货地址列表")
    private List<UmsMemberReceiveAddress> memberReceiveAddressList;
    /**
     * 用户可用优惠券列表
     */
    @ApiModelProperty("用户可用优惠券列表")
    private List<SmsCouponHistoryDetail> couponHistoryDetailList;
    /**
     * 积分使用规则
     */
    @ApiModelProperty("积分使用规则")
    private UmsIntegrationConsumeSetting integrationConsumeSetting;
    /**
     * 会员持有的积分
     */
    @ApiModelProperty("会员持有的积分")
    private Integer memberIntegration;
    /**
     * 计算的金额
     */
    @ApiModelProperty("计算的金额")
    private CalcAmount calcAmount;

    @Getter
    @Setter
    public static class CalcAmount {
        /**
         * 订单商品总金额
         */
        @ApiModelProperty("订单商品总金额")
        private BigDecimal totalAmount;
        /**
         * 运费
         */
        @ApiModelProperty("运费")
        private BigDecimal freightAmount;
        /**
         * 活动优惠
         */
        @ApiModelProperty("活动优惠")
        private BigDecimal promotionAmount;
        /**
         * 应付金额
         */
        @ApiModelProperty("应付金额")
        private BigDecimal payAmount;
    }
}
