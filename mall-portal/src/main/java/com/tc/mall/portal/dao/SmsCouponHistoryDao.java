package com.tc.mall.portal.dao;

import com.tc.mall.model.SmsCoupon;
import com.tc.mall.portal.domain.SmsCouponHistoryDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员优惠券领取记录管理自定义Dao
 *
 * @author honggang.liu
 */
public interface SmsCouponHistoryDao {
    /**
     * 获取优惠券历史详情
     *
     * @param memberId 会员ID
     * @return 优惠券历史详情
     */
    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);

    /**
     * 获取指定会员优惠券列表
     *
     * @param memberId  会员ID
     * @param useStatus 使用状态
     * @return 优惠券列表
     */
    List<SmsCoupon> getCouponList(@Param("memberId") Long memberId, @Param("useStatus") Integer useStatus);
}
