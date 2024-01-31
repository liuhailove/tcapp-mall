package com.tc.mall.portal.domain;

import com.tc.mall.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 前台商品详情
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class PmsPortalProductDetail {
    /**
     * 商品信息
     */
    @ApiModelProperty("商品信息")
    private PmsProduct product;

    /**
     * 商品品牌
     */
    @ApiModelProperty("商品品牌")
    private PmsBrand brand;

    /**
     * 商品属性与参数
     */
    @ApiModelProperty("商品属性与参数")
    private List<PmsProductAttribute> productAttributeList;

    /**
     * 手动录入的商品属性与参数值
     */
    @ApiModelProperty("手动录入的商品属性与参数值")
    private List<PmsProductAttributeValue> productAttributeValueList;

    /**
     * 商品的sku库存信息
     */
    @ApiModelProperty("商品的sku库存信息")
    private List<PmsSkuStock> skuStockList;

    /**
     * 商品阶梯价格设置
     */
    @ApiModelProperty("商品阶梯价格设置")
    private List<PmsProductLadder> productLadderList;

    /**
     * 商品满减价格设置
     */
    @ApiModelProperty("商品满减价格设置")
    private List<PmsProductFullReduction> productFullReductionList;

    /**
     * 商品可用优惠券
     */
    @ApiModelProperty("商品可用优惠券")
    private List<SmsCoupon> couponList;
}
