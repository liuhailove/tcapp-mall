package com.tc.mall.admin.service;


import com.tc.mall.model.UmsResource;

import java.util.List;

/**
 * 后台资源管理Service
 *
 * @author honggang.liu
 */
public interface UmsResourceService {
    /**
     * 添加资源
     *
     * @param umsResource 资源
     * @return 添加结果
     */
    int create(UmsResource umsResource);

    /**
     * 修改资源
     *
     * @param id          资源ID
     * @param umsResource 资源
     */
    int update(Long id, UmsResource umsResource);

    /**
     * 获取资源详情
     *
     * @param id 获取资源ID
     * @return 资源
     */
    UmsResource getItem(Long id);

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 删除结果
     */
    int delete(Long id);

    /**
     * 分页查询资源
     *
     * @param categoryId  品类id
     * @param nameKeyword 名称关键字
     * @param urlKeyword  url关键字
     * @param pageSize    分页大小
     * @param pageNum     页码
     * @return 资源列表
     */
    List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * 查询全部资源
     */
    List<UmsResource> listAll();
}
