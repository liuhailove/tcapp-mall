package com.tc.mall.portal.service;

import com.tc.mall.model.OmsCartItem;
import com.tc.mall.portal.domain.CartPromotionItem;

import java.util.List;

/**
 * 促销管理Service
 *
 * @author honggang.liu
 */
public interface OmsPromotionService {

    /**
     * 计算购物车中的促销活动信息
     *
     * @param cartItemList 购物车
     * @return 促销活动信息
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
