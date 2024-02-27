package com.tc.mall.portal.domain;

import com.tc.mall.model.PmsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 秒杀信息和商品对象封装
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class FlashPromotionProduct extends PmsProduct {
    /**
     * 秒杀价格
     */
    @ApiModelProperty("秒杀价格")
    private BigDecimal flashPromotionPrice;
    /**
     * 用于秒杀到数量
     */
    @ApiModelProperty("用于秒杀到数量")
    private Integer flashPromotionCount;
    /**
     * 秒杀限购数量
     */
    @ApiModelProperty("秒杀限购数量")
    private Integer flashPromotionLimit;
}
