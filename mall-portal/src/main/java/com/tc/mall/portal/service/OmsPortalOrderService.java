package com.tc.mall.portal.service;

import com.tc.mall.portal.domain.ConfirmOrderResult;

import java.util.List;

/**
 * 前台订单管理
 *
 * @author honggang.liu
 */
public interface OmsPortalOrderService {
    /**
     * 根据用户购物车信息生成确认单信息
     *
     * @param cartIds 购物车商品Ids
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);
}
