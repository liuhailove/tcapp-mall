package com.tc.mall.portal.dao;

import com.tc.mall.model.OmsOrderItem;
import com.tc.mall.portal.domain.OmsOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前台订单管理自定义Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface PortalOrderDao {
    /**
     * 获取订单及下单商品详情
     *
     * @param orderId 订单ID
     * @return 订单及下单商品详情
     */
    OmsOrderDetail getDetail(@Param("orderId") Long orderId);

    /**
     * 修改 pms_sku_stock表的锁定库存及真实库存
     *
     * @param orderItemList 订单列表
     * @return 影响行数
     */
    int updateSkuStock(@Param("itemList") List<OmsOrderItem> orderItemList);

    /**
     * 获取超时订单
     *
     * @param minute 超时时间（分)
     * @return 超时订单
     */
    List<OmsOrderDetail> getTimeOutOrders(@Param("minute") Integer minute);

    /**
     * 批量修改订单状态
     *
     * @param ids    订单ID
     * @param status 状态
     * @return 影响行数
     */
    int updateOrderStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 解除取消订单的库存锁定
     *
     * @param orderItemList 订单列表
     * @return 影响行数
     */
    int releaseSkuStockLock(@Param("itemList") List<OmsOrderItem> orderItemList);
}
