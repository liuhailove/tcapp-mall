package com.tc.mall.portal.domain;

import com.tc.mall.model.SmsCoupon;
import com.tc.mall.model.SmsCouponHistory;
import com.tc.mall.model.SmsCouponProductCategoryRelation;
import com.tc.mall.model.SmsCouponProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 优惠券领取历史详情（包括优惠券信息和关联关系）
 *
 * @author honggang.liu
 */
@Getter
@Setter
public class SmsCouponHistoryDetail extends SmsCouponHistory {
    /**
     * 相关优惠券信息
     */
    @ApiModelProperty("相关优惠券信息")
    private SmsCoupon coupon;

    /**
     * 优惠券关联商品
     */
    @ApiModelProperty("优惠券关联商品")
    private List<SmsCouponProductRelation> productRelationList;

    /**
     * 优惠券关联商品分类
     */
    @ApiModelProperty("优惠券关联商品分类")
    private List<SmsCouponProductCategoryRelation> categoryRelationsList;
}
