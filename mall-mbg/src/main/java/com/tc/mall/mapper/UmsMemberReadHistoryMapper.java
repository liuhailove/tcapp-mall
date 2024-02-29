package com.tc.mall.mapper;

import com.tc.mall.model.UmsMemberReadHistory;
import com.tc.mall.model.UmsMemberReadHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberReadHistoryMapper {
    long countByExample(UmsMemberReadHistoryExample example);

    int deleteByExample(UmsMemberReadHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberReadHistory row);

    int insertSelective(UmsMemberReadHistory row);

    List<UmsMemberReadHistory> selectByExample(UmsMemberReadHistoryExample example);

    UmsMemberReadHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsMemberReadHistory row, @Param("example") UmsMemberReadHistoryExample example);

    int updateByExample(@Param("row") UmsMemberReadHistory row, @Param("example") UmsMemberReadHistoryExample example);

    int updateByPrimaryKeySelective(UmsMemberReadHistory row);

    int updateByPrimaryKey(UmsMemberReadHistory row);
}