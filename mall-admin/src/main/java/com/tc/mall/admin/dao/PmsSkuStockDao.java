package com.tc.mall.admin.dao;

import com.tc.mall.model.PmsSkuStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品SKU管理自定义Dao
 */
@Mapper
public interface PmsSkuStockDao {
    /**
     * 批量插入操作
     *
     * @param skuStockList sku库存信息
     * @return 影响行数
     */
    int insertList(@Param("list") List<PmsSkuStock> skuStockList);

    /**
     * 批量插入或者替换操作
     *
     * @param skuStockList sku库存信息
     * @return 影响行数
     */
    int replaceList(@Param("list") List<PmsSkuStock> skuStockList);
}
