package com.tc.mall.portal.service;

import com.tc.mall.model.UmsMemberReadHistory;

import java.util.List;

/**
 * 会员浏览记录管理Service
 *
 * @author honggang.liu
 */
public interface MemberReadHistoryService {

    /**
     * 生成浏览记录
     *
     * @param memberReadHistory 会员浏览历史
     * @return 操作影响行数
     */
    int create(UmsMemberReadHistory memberReadHistory);

    /**
     * 批量删除浏览记录
     *
     * @param ids 主键ID集合
     * @return 影响行数
     */
    int delete(List<String> ids);

    /**
     * 分页获取用户浏览历史记录
     *
     * @param pageNum  页码
     * @param pageSize 分页大小
     * @return 分页数
     */
    List<UmsMemberReadHistory> list(Integer pageNum, Integer pageSize);

    /**
     * 清空浏览记录
     */
    void clear();
}
