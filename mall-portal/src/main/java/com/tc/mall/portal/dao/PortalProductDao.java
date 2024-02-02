package com.tc.mall.portal.dao;

import com.tc.mall.model.SmsCoupon;
import com.tc.mall.portal.domain.CartProduct;
import com.tc.mall.portal.domain.PromotionProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前台购物车商品管理自定义
 *
 * @author honggang.liu
 */
@Mapper
public interface PortalProductDao {
    /**
     * 获取购物车商品信息
     *
     * @param id 商品Id
     * @return 返回购物车中商品
     */
    CartProduct getCartProduct(@Param("id") Long id);

    /**
     * 获取促销商品信息列表
     *
     * @param ids 商品集合
     * @return 促销商品集合
     */
    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    /**
     * 获取可用优惠券列表
     *
     * @param productId         商品Id
     * @param productCategoryId 品类Id
     * @return 可用券
     */
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId") Long productCategoryId);
}
