package com.tc.mall.admin.dto;

import com.tc.mall.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 创建和修改商品的请求参数
 *
 * @author honggang.liu
 */
@Data
@EqualsAndHashCode
public class PmsProductParam extends PmsProduct {
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
     * 商品会员价格设置
     */
    @ApiModelProperty("商品会员价格设置")
    private List<PmsMemberPrice> memberPriceList;

    /**
     * 商品的sku库存信息
     */
    private List<PmsSkuStock> skuStockList;

    /**
     * 商品参数及自定义规格属性
     */
    @ApiModelProperty("商品参数及自定义规格属性")
    private List<PmsProductAttributeValue> productAttributeValueList;

    /**
     * 专题和商品关系
     */
    @ApiModelProperty("专题和商品关系")
    private List<CmsSubjectProductRelation> subjectProductRelationList;

    /**
     * 优选专区和商品的关系
     */
    @ApiModelProperty("优选专区和商品的关系")
    private List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList;
}
