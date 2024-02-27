package com.tc.mall.portal.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 生成订单时传入的参数
 *
 * @author honggang.liu
 */
@Data
@EqualsAndHashCode
public class OrderParam {
    /**
     * 收货地址ID
     */
    @ApiModelProperty("收货地址ID")
    private Long memberReceiveAddressId;
    /**
     * 优惠券ID
     */
    @ApiModelProperty("优惠券ID")
    private Long couponId;
    /**
     * 使用的积分数
     */
    @ApiModelProperty("使用的积分数")
    private Integer useIntegration;
    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    private Integer payType;
    /**
     * 被选中的购物车商品ID
     */
    @ApiModelProperty("被选中的购物车商品ID")
    private List<Long> cartIds;
}
