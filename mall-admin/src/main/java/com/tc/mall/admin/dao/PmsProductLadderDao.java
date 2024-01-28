package com.tc.mall.admin.dao;

import com.tc.mall.model.PmsProductLadder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员阶梯价格自定义
 *
 * @author honggang.liu
 */
@Mapper
public interface PmsProductLadderDao {
    /**
     * 批量创建
     *
     * @param productLadderList 阶梯价格List
     * @return 影响行数
     */
    int insertList(@Param("list") List<PmsProductLadder> productLadderList);
}
