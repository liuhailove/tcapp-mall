package com.tc.mall.portal.service;

import com.tc.mall.model.UmsMemberProductCollection;

import java.util.List;

/**
 * 会员商品收藏管理Service
 *
 * @author honggang.liu
 */
public interface MemberCollectionService {
    /**
     * 添加收藏
     */
    int add(UmsMemberProductCollection productCollection);

    /**
     * 删除收藏
     */
    int delete(Long productId);

    /**
     * 分页查询收藏
     */
    List<UmsMemberProductCollection> list(Integer pageNum, Integer pageSize);

    /**
     * 查看收藏详情
     */
    UmsMemberProductCollection detail(Long productId);

    /**
     * 清空收藏
     */
    void clear();
}
