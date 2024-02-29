package com.tc.mall.portal.service;

import com.tc.mall.model.UmsMemberBrandAttention;

import java.util.List;

/**
 * 会员品牌关注管理Service
 *
 * @author honggang.liu
 */
public interface MemberAttentionService {

    /**
     * 添加关注
     *
     * @param umsMemberBrandAttention 关注model
     * @return 影响行数
     */
    int add(UmsMemberBrandAttention umsMemberBrandAttention);

    /**
     * 取消关注
     *
     * @param brandId 品牌ID
     * @return 影响行数
     */
    int delete(Long brandId);

    /**
     * 获取用户关注列表
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 分页列表
     */
    List<UmsMemberBrandAttention> list(Integer pageNum, Integer pageSize);

    /**
     * 获取用户关注详情
     *
     * @param brandId 品牌ID
     * @return 关注详细
     */
    UmsMemberBrandAttention detail(Long brandId);

    /**
     * 清空关注列表
     */
    void clear();
}
