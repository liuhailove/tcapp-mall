package com.tc.mall.portal.service;

import com.tc.mall.model.SmsCoupon;
import com.tc.mall.model.SmsCouponHistory;
import com.tc.mall.portal.domain.CartPromotionItem;
import com.tc.mall.portal.domain.SmsCouponHistoryDetail;

import java.util.List;

/**
 * 用户优惠券管理Service
 *
 * @author honggang.liu
 */
public interface UmsMemberCouponService {
    /**
     * 会员添加优惠券
     *
     * @param couponId 券ID
     */
    void add(Long couponId);

    /**
     * 获取优惠券历史列表
     *
     * @param useStatus 使用状态
     * @return 优惠券历史列表
     */
    List<SmsCouponHistory> listHistory(Integer useStatus);

    /**
     * 根据购物车信息获取可用优惠券
     *
     * @param cartItemList 购物车列表
     * @param type         类型
     * @return 优惠券历史列表
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type);

    /**
     * 获取商品Id的优惠券
     *
     * @param productId 商品ID
     * @return 券列表
     */
    List<SmsCoupon> listByProduct(Long productId);

    /**
     * 根据使用状态获取优惠券列表
     *
     * @param useStatus 使用状态
     * @return 优惠券列表
     */
    List<SmsCoupon> list(Integer useStatus);
}
