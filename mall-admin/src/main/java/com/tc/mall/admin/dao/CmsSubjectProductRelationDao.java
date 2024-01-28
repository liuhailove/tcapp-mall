package com.tc.mall.admin.dao;

import com.tc.mall.model.CmsSubjectProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品和专题关系自定义DAO
 *
 * @author honggang.liu
 */
public interface CmsSubjectProductRelationDao {
    /**
     * 批量创建
     *
     * @param subjectProductRelationList 商品和主题关系
     * @return 影响行数
     */
    int insertList(@Param("list") List<CmsSubjectProductRelation> subjectProductRelationList);
}
