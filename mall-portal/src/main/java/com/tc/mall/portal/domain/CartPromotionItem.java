package com.tc.mall.portal.domain;

import com.tc.mall.model.OmsCartItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 购物车中促销信息的封装
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class CartPromotionItem extends OmsCartItem {

    /**
     * 促销活动信息
     */
    @ApiModelProperty("促销活动信息")
    private String promotionMessage;
    /**
     * 促销活动减去的金额，针对每个商品
     */
    @ApiModelProperty("促销活动减去的金额，针对每个商品")
    private BigDecimal reduceAmount;
    /**
     * 剩余库存-锁定库存
     */
    @ApiModelProperty("剩余库存-锁定库存")
    private Integer realStock;
    /**
     * 购买商品赠送积分
     */
    @ApiModelProperty("购买商品赠送积分")
    private Integer integration;
    /**
     * 购买商品赠送成长值
     */
    @ApiModelProperty("购买商品赠送成长值")
    private Integer growth;
}
