package com.tc.mall.admin.dao;

import com.tc.mall.model.PmsProductFullReduction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品满减自定义Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface PmsProductFullReductionDao {

    /**
     * 批量创建
     *
     * @param productFullReductionList 满减产品列表
     * @return 影响行数
     */
    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
