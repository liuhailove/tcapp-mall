package com.tc.mall.mapper;

import com.tc.mall.model.UmsMemberProductCollection;
import com.tc.mall.model.UmsMemberProductCollectionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsMemberProductCollectionMapper {
    long countByExample(UmsMemberProductCollectionExample example);

    int deleteByExample(UmsMemberProductCollectionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMemberProductCollection row);

    int insertSelective(UmsMemberProductCollection row);

    List<UmsMemberProductCollection> selectByExample(UmsMemberProductCollectionExample example);

    UmsMemberProductCollection selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsMemberProductCollection row, @Param("example") UmsMemberProductCollectionExample example);

    int updateByExample(@Param("row") UmsMemberProductCollection row, @Param("example") UmsMemberProductCollectionExample example);

    int updateByPrimaryKeySelective(UmsMemberProductCollection row);

    int updateByPrimaryKey(UmsMemberProductCollection row);
}