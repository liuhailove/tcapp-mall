package com.tc.mall.security.component;

import cn.hutool.core.util.URLUtil;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 动态权限数据源，用于获取动态权限规则
 *
 * @author honggang.liu
 */
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /**
     * 配置的属性map
     */
    private static Map<String, ConfigAttribute> configAttributeMap = null;

    /**
     * 动态权限资源服务
     */
    @Resource
    private DynamicSecurityService dynamicSecurityService;

    @PostConstruct
    public void loadDataSource() {
        configAttributeMap = dynamicSecurityService.loadDataSource();
    }

    public void clearDataSource() {
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
       if(configAttributeMap==null){
           this.loadDataSource();
       }
        List<ConfigAttribute> configAttributes=new ArrayList<>();
        //获取当前访问的路径
        String url=((FilterInvocation)object).getRequestUrl();
        String path= URLUtil.getPath(url);
        PathMatcher pathMatcher = new AntPathMatcher();
        //获取访问该路径所需资源
        for (Map.Entry<String,ConfigAttribute> entry : configAttributeMap.entrySet()) {
            if (pathMatcher.match(entry.getKey(), path)) {
                configAttributes.add(entry.getValue());
            }
        }
        // 未设置操作请求权限，返回空集合
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
