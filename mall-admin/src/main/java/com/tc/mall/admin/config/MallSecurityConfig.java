package com.tc.mall.admin.config;

import com.tc.mall.admin.service.UmsAdminService;
import com.tc.mall.admin.service.UmsResourceService;
import com.tc.mall.model.UmsResource;
import com.tc.mall.security.component.DynamicSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mall-security模块相关配置
 *
 * @author honggang.liu
 */
@Configuration
public class MallSecurityConfig {

    /**
     * 用户admin
     */
    @Resource
    private UmsAdminService adminService;
    /**
     * 资源信息
     */
    @Resource
    private UmsResourceService resourceService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> adminService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return () -> {
            Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
            List<UmsResource> resourceList = resourceService.listAll();
            for (UmsResource resource : resourceList) {
                map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
            }
            return map;
        };
    }
}
