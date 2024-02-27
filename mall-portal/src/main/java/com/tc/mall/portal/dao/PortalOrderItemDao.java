package com.tc.mall.portal.dao;

import com.tc.mall.model.OmsOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单商品信息管理自定义Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface PortalOrderItemDao {
    /**
     * 批量插入
     *
     * @param list 订单Sku
     * @return 影响行数
     */
    int insertList(@Param("list") List<OmsOrderItem> list);
}
