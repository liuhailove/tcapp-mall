package com.tc.mall.portal.config;

import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

/**
 * mall-security模块相关配置
 *
 * @author honggang.liu
 */
@Configuration
public class MallSecurityConfig {

    /**
     * 会员服务
     */
    @Resource
    private UmsMemberService memberService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> memberService.loadUserByUsername(username);
    }
}
