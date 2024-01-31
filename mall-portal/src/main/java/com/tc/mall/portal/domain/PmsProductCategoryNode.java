package com.tc.mall.portal.domain;

import com.tc.mall.model.PmsProductCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 包含子分类的商品分类
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class PmsProductCategoryNode extends PmsProductCategory {
    /**
     * 子分类集合
     */
    @ApiModelProperty("子分类集合")
    private List<PmsProductCategoryNode> children;
}
