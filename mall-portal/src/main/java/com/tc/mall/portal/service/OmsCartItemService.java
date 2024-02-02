package com.tc.mall.portal.service;

import com.tc.mall.model.OmsCartItem;
import com.tc.mall.portal.domain.CartProduct;
import com.tc.mall.portal.domain.CartPromotionItem;

import java.util.List;

/**
 * 购物车管理Service
 *
 * @author honggang.liu
 */
public interface OmsCartItemService {
    /**
     * 查询购物车中是否包含该商品，有增加数量，无添加到购物车
     *
     * @param cartItem 购物车商品
     * @return 影响行数
     */
    int add(OmsCartItem cartItem);

    /**
     * 根据会员编号获取购物车列表
     *
     * @param memberId 会员编号
     * @return 购物车列表
     */
    List<OmsCartItem> list(Long memberId);

    /**
     * 获取包含促销活动信息的购物车列表
     *
     * @param memberId 会员ID
     * @param cartIds  购物车
     * @return 获取包含促销活动信息的购物车列表
     */
    List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds);

    /**
     * 修改某个购物车商品的数量
     *
     * @param id       商品id
     * @param memberId 会员ID
     * @param quality  商品数量
     * @return 修改结果
     */
    int updateQuality(Long id, Long memberId, Integer quality);

    /**
     * 批量删除购物车中的商品
     *
     * @param memberId 会员id
     * @param ids      商品集合
     * @return 删除结果
     */
    int delete(Long memberId, List<Long> ids);

    /**
     * 获取购物车中用于选择商品规格的商品信息
     *
     * @param product 商品id
     * @return s商品信息
     */
    CartProduct getCartProduct(Long product);

    /**
     * 修改购物车中商品的规格
     *
     * @param cartItem 购物车商品规则
     * @return 修改结果
     */
    int updateAttr(OmsCartItem cartItem);

    /**
     * 清空购物车
     *
     * @param memberId 会员ID
     * @return 清空结果
     */
    int clear(Long memberId);
}
