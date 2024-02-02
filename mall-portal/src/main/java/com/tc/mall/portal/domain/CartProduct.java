package com.tc.mall.portal.domain;

import com.tc.mall.model.PmsProduct;
import com.tc.mall.model.PmsProductAttribute;
import com.tc.mall.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车中带规格和SKU的商品信息
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class CartProduct extends PmsProduct {
    /**
     * 商品属性列表
     */
    @ApiModelProperty("商品属性列表")
    private List<PmsProductAttribute> productAttributeList;
    /**
     * 商品SKU库存列表
     */
    @ApiModelProperty("商品SKU库存列表")
    private List<PmsSkuStock> skuStockList;
}
