package com.tc.mall.admin.dao;

import com.tc.mall.model.PmsMemberPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员价格管理自定义DAO
 *
 * @author honggang.liu
 */
@Mapper
public interface PmsMemberPriceDao {

    /**
     * 批量创建
     *
     * @param memberPriceList 会员价格列表
     * @return 影响行数
     */
    int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}
