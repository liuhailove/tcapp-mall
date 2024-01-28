package com.tc.mall.admin.dao;

import com.tc.mall.model.CmsPrefrenceAreaProductRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优选和商品关系自定义Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface CmsPrefrenceAreaProductRelationDao {
    /**
     * 批量创建
     *
     * @param prefrenceAreaProductRelationList 优选和商品关系
     * @return 影响行数
     */
    int insertList(@Param("list") List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList);
}
