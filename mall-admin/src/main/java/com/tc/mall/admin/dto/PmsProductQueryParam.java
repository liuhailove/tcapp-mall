package com.tc.mall.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品查询参数
 *
 * @author honggang.liu
 */
@Data
@EqualsAndHashCode
public class PmsProductQueryParam implements Serializable {
    /**
     * 上架状态
     */
    @ApiModelProperty(value = "上架状态")
    private Integer publishStatus;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Integer verifyStatus;

    /**
     * 商品名称模糊关键字
     */
    @ApiModelProperty(value = "商品名称模糊关键字")
    private String keyword;

    /**
     * 商品货号
     */
    @ApiModelProperty(value = "商品货号")
    private String productSn;

    /**
     * 商品分类编号
     */
    @ApiModelProperty(value = "商品分类编号")
    private Long productCategoryId;

    /**
     * 商品品牌编号
     */
    @ApiModelProperty(value = "商品品牌编号")
    private Long brandId;
}
