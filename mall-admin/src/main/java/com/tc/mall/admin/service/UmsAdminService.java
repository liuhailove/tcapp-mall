package com.tc.mall.admin.service;

import com.tc.mall.admin.dto.UmsAdminParam;
import com.tc.mall.model.UmsAdmin;
import com.tc.mall.model.UmsResource;
import com.tc.mall.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台用户管理Service
 *
 * @author honggang.liu
 */
public interface UmsAdminService {
    /**
     * 根据用户名获取后台管理员
     *
     * @param username 用户名称
     * @return 后台用
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     *
     * @param umsAdminParam 注册参数
     * @return 后台用户
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 刷新token的功能
     *
     * @param oldToken 旧的token
     * @return 新的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户id获取用户
     *
     * @param id 用户ID
     * @return 后台用户
     */
    UmsAdmin getItem(Long id);

    /**
     * 根据用户名或昵称分页查询用户
     *
     * @param keyword  搜索关键词
     * @param pageSize 分页大小
     * @param pageNum  页码
     * @return 后台用户列表
     */
    List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     *
     * @param id    后台用户ID
     * @param admin 用户信息
     * @return 更新结果
     */
    int update(Long id, UmsAdmin admin);

    /**
     * 删除指定用户
     *
     * @param id 删除用户
     * @return 删除结果
     */
    int delete(Long id);

    /**
     * 修改用户角色关系
     *
     * @param adminId 后台用户ID
     * @param roleIds 角色ID
     * @return 更细结果
     */
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对应角色
     *
     * @param adminId 用户ID
     * @return 角色列表
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     *
     * @param adminId 后台adminId
     * @return 可以访问的资源
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 修改密码
     *
     * @param updatePasswordParam 更新密码参数
     * @return 更新结果
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     *
     * @param username 用户名称
     * @return 用户明细
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 获取缓存服务
     *
     * @return 获取缓存服务
     */
    UmsAdminCacheService getCacheService();
}
