package com.tc.mall.portal.service.impl;

import com.tc.mall.mapper.OmsCartItemMapper;
import com.tc.mall.model.OmsCartItem;
import com.tc.mall.model.OmsCartItemExample;
import com.tc.mall.model.UmsMember;
import com.tc.mall.portal.dao.PortalProductDao;
import com.tc.mall.portal.domain.CartProduct;
import com.tc.mall.portal.domain.CartPromotionItem;
import com.tc.mall.portal.service.OmsCartItemService;
import com.tc.mall.portal.service.OmsPromotionService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {

    /**
     * 购物车商品Mapper
     */
    @Resource
    private OmsCartItemMapper cartItemMapper;

    /**
     * 商品Dao
     */
    @Resource
    private PortalProductDao productDao;

    /**
     * 订单促销服务
     */
    @Resource
    private OmsPromotionService promotionService;

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 查询购物车中是否包含该商品，有增加数量，无添加到购物车
     *
     * @param cartItem 购物车商品
     * @return 影响行数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember currentMember = memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            count = cartItemMapper.insert(cartItem);
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            count = cartItemMapper.updateByPrimaryKey(existCartItem);
        }
        return count;
    }

    /**
     * 根据会员id,商品id和规格获取购物车中商品
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(cartItem.getMemberId())
                .andProductIdEqualTo(cartItem.getProductId())
                .andDeleteStatusEqualTo(0);
        if (cartItem.getProductSkuId() != 0) {
            criteria.andProductSkuIdEqualTo(cartItem.getProductSkuId());
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }

    /**
     * 根据会员编号获取购物车列表
     *
     * @param memberId 会员编号
     * @return 购物车列表
     */
    @Override
    public List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
        return cartItemMapper.selectByExample(example);
    }

    /**
     * 获取包含促销活动信息的购物车列表
     *
     * @param memberId 会员ID
     * @param cartIds  购物车
     * @return 获取包含促销活动信息的购物车列表
     */
    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> cartItemList = list(memberId);
        if (!CollectionUtils.isEmpty(cartIds)) {
            cartItemList = cartItemList.stream().filter(item -> cartIds.contains(item.getId())).collect(Collectors.toList());
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cartItemList)) {
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    /**
     * 修改某个购物车商品的数量
     *
     * @param id       商品id
     * @param memberId 会员ID
     * @param quantity  商品数量
     * @return 修改结果
     */
    @Override
    public int updateQuality(Long id, Long memberId, Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andIdEqualTo(id)
                .andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    /**
     * 批量删除购物车中的商品
     *
     * @param memberId 会员id
     * @param ids      商品集合
     * @return 删除结果
     */
    @Override
    public int delete(Long memberId, List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record, example);
    }

    /**
     * 获取购物车中用于选择商品规格的商品信息
     *
     * @param productId 商品id
     * @return s商品信息
     */
    @Override
    public CartProduct getCartProduct(Long productId) {
        return productDao.getCartProduct(productId);
    }

    /**
     * 修改购物车中商品的规格
     *
     * @param cartItem 购物车商品规则
     * @return 修改结果
     */
    @Override
    public int updateAttr(OmsCartItem cartItem) {
        // 删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateByPrimaryKeySelective(updateCart);
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    /**
     * 清空购物车
     *
     * @param memberId 会员ID
     * @return 清空结果
     */
    @Override
    public int clear(Long memberId) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record, example);
    }
}
