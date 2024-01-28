package com.tc.mall.admin.service;


import com.tc.mall.model.UmsAdmin;
import com.tc.mall.model.UmsResource;

import java.util.List;

/**
 * 后台用户缓存管理Service
 *
 * @author honggang.liu
 */
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     *
     * @param adminId 用户ID
     */
    void delAdmin(Long adminId);

    /**
     * 删除后台用户资源列表缓存
     *
     * @param adminId 后台用户ID
     */
    void delResourceList(Long adminId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     *
     * @param roleId 角色ID
     */
    void delResourceListByRole(Long roleId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     *
     * @param roleIds 角色集合
     */
    void delResourceListByRoleIds(List<Long> roleIds);

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     *
     * @param resourceId 资源ID
     */
    void delResourceListByResource(Long resourceId);

    /**
     * 获取缓存后台用户信息
     *
     * @param username 用户名称
     * @return 用户信息
     */
    UmsAdmin getAdmin(String username);

    /**
     * 设置缓存后台用户信息
     *
     * @param admin 后台用户
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 获取缓存后台用户资源列表
     *
     * @param adminId 用户ID
     * @return 资源列表
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 设置缓存后台用户资源列表
     *
     * @param adminId      后台用户ID
     * @param resourceList 资源列表
     */
    void setResourceList(Long adminId, List<UmsResource> resourceList);
}
