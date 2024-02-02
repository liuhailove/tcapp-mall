package com.tc.mall.portal.service.impl;

import com.tc.mall.common.exception.Asserts;
import com.tc.mall.mapper.SmsCouponHistoryMapper;
import com.tc.mall.mapper.SmsCouponMapper;
import com.tc.mall.mapper.SmsCouponProductCategoryRelationMapper;
import com.tc.mall.mapper.SmsCouponProductRelationMapper;
import com.tc.mall.model.SmsCoupon;
import com.tc.mall.model.SmsCouponHistory;
import com.tc.mall.model.SmsCouponHistoryExample;
import com.tc.mall.model.UmsMember;
import com.tc.mall.portal.dao.SmsCouponHistoryDao;
import com.tc.mall.portal.domain.CartPromotionItem;
import com.tc.mall.portal.domain.SmsCouponHistoryDetail;
import com.tc.mall.portal.service.UmsMemberCouponService;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 会员优惠券管理Service实现类
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    /**
     * 券mapper
     */
    @Resource
    private SmsCouponMapper couponMapper;

    /**
     * 券历史
     */
    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    /**
     * 券历史Dao
     */
    @Resource
    private SmsCouponHistoryDao couponHistoryDao;

    /**
     * 券商品关系
     */
    @Resource
    private SmsCouponProductRelationMapper couponProductRelationMapper;

    /**
     * 券商品类目关系Mapper
     */
    @Resource
    private SmsCouponProductCategoryRelationMapper smsCouponProductCategoryRelationMapper;


    /**
     * 会员添加优惠券
     *
     * @param couponId 券ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(Long couponId) {
        UmsMember currentMember = memberService.getCurrentMember();
        // 获取优惠券信息，判断数量
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if (coupon == null) {
            Asserts.fail("优惠券不存在");
        }
        if (coupon.getCount() <= 0) {
            Asserts.fail("优惠券已经领完了");
        }
        Date now = new Date();
        if (now.before(coupon.getEnableTime())) {
            Asserts.fail("优惠券还没到领取时间");
        }
        // 判断用户领取的优惠券数量是否超过限制
        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        couponHistoryExample.createCriteria().andCouponIdEqualTo(couponId).andMemberIdEqualTo(currentMember.getId());
        long count = couponHistoryMapper.countByExample(couponHistoryExample);
        if (count >= coupon.getCount()) {
            Asserts.fail("您已经领取过该优惠券");
        }
        // 生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        // 主动领取
        couponHistory.setGetType(1);
        // 未使用
        couponHistory.setUseStatus(0);
        // 修改优惠券表的数量、领取数量
        coupon.setCount(coupon.getCount() - 1);
        coupon.setReceiveCount(coupon.getReceiveCount() == null ? 1 : coupon.getReceiveCount() + 1);
        couponMapper.updateByPrimaryKey(coupon);
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = Long.toString(currentTimeMillis);
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length()-4));
        }
        return sb.toString();
    }

    /**
     * 获取优惠券历史列表
     *
     * @param useStatus 使用状态
     * @return 优惠券历史列表
     */
    @Override
    public List<SmsCouponHistory> listHistory(Integer useStatus) {
        return null;
    }

    /**
     * 根据购物车信息获取可用优惠券
     *
     * @param cartItemList 购物车列表
     * @param type         类型
     * @return 优惠券历史列表
     */
    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type) {
        return null;
    }

    /**
     * 获取商品Id的优惠券
     *
     * @param productId 商品ID
     * @return 券列表
     */
    @Override
    public List<SmsCoupon> listByProduct(Long productId) {
        return null;
    }

    /**
     * 根据使用状态获取优惠券列表
     *
     * @param useStatus 使用状态
     * @return 优惠券列表
     */
    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        return null;
    }
}
