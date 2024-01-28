package com.tc.mall.model;

import java.io.Serializable;

/**
 * 优选专区和产品关系
 * @author honggang.liu
 */
public class CmsPrefrenceAreaProductRelation implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 优选专区ID
     */
    private Long prefrenceAreaId;

    /**
     * 产品ID
     */
    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrefrenceAreaId() {
        return prefrenceAreaId;
    }

    public void setPrefrenceAreaId(Long prefrenceAreaId) {
        this.prefrenceAreaId = prefrenceAreaId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "CmsPrefrenceAreaProductRelation{" +
                "id=" + id +
                ", prefrenceAreaId=" + prefrenceAreaId +
                ", productId=" + productId +
                '}';
    }
}
