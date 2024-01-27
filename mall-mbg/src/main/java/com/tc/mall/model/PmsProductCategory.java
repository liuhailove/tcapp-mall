package com.tc.mall.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 产品品类
 *
 * @author honggang.liu
 */
public class PmsProductCategory implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 品类名称
     */
    private String name;

    /**
     * 分类级别
     */
    @ApiModelProperty(value = "分类级别：0->1级；1->2级")
    private Integer level;

    /**
     * 产品数量
     */
    private Integer productCount;

    /**
     * 产品单位
     */
    private String productUint;

    /**
     * 是否显示在导航栏
     */
    @ApiModelProperty(value = "是否显示在导航栏：0->不显示；1->显示")
    private Integer navStatus;

    /**
     * 显示状态
     */
    @ApiModelProperty(value = "显示状态：0->不显示；1->显示")
    private Integer showStatus;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getProductUint() {
        return productUint;
    }

    public void setProductUint(String productUint) {
        this.productUint = productUint;
    }

    public Integer getNavStatus() {
        return navStatus;
    }

    public void setNavStatus(Integer navStatus) {
        this.navStatus = navStatus;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
