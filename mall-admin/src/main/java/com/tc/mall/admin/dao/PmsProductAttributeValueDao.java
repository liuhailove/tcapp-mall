package com.tc.mall.admin.dao;

import com.tc.mall.model.PmsProductAttributeValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性值管理自定义Dao
 */
@Mapper
public interface PmsProductAttributeValueDao {
    /**
     * 批量创建
     *
     * @param productAttributeValueList 商品属性值
     * @return 影响行数
     */
    int insertList(@Param("list") List<PmsProductAttributeValue> productAttributeValueList);
}
