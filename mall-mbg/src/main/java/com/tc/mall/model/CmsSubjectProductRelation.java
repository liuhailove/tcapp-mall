package com.tc.mall.model;

import java.io.Serializable;

/**
 * 专题产品关系
 */
public class CmsSubjectProductRelation implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 专题
     */
    private Long subjectId;

    /**
     * 产品Id
     */
    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "CmsSubjectProductRelation{" +
                "id=" + id +
                ", subjectId=" + subjectId +
                ", productId=" + productId +
                '}';
    }
}
