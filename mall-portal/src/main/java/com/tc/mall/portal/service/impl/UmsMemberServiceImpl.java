package com.tc.mall.portal.service.impl;

import com.tc.mall.model.UmsMember;
import com.tc.mall.portal.domain.MemberDetails;
import com.tc.mall.portal.service.UmsMemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 会员服务
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    /**
     * 根据用户名获取会员
     *
     * @param username
     */
    @Override
    public UmsMember getByUsername(String username) {
        return null;
    }

    /**
     * 根据会员编号获取会员
     *
     * @param id
     */
    @Override
    public UmsMember getById(Long id) {
        return null;
    }

    /**
     * 用户注册
     *
     * @param username
     * @param password
     * @param telephone
     * @param authCode
     */
    @Override
    public void register(String username, String password, String telephone, String authCode) {

    }

    /**
     * 生成验证码
     *
     * @param telephone
     */
    @Override
    public String generateAuthCode(String telephone) {
        return null;
    }

    /**
     * 修改密码
     *
     * @param telephone
     * @param password
     * @param authCode
     */
    @Override
    public void updatePassword(String telephone, String password, String authCode) {

    }

    /**
     * 获取当前登录会员
     */
    @Override
    public UmsMember getCurrentMember() {
        SecurityContext ctx= SecurityContextHolder.getContext();
        Authentication auth=ctx.getAuthentication();
        MemberDetails memberDetails=(MemberDetails) auth.getPrincipal();
        return memberDetails.getUmsMember();
    }

    /**
     * 根据会员id修改会员积分
     *
     * @param id
     * @param integration
     */
    @Override
    public void updateIntegration(Long id, Integer integration) {

    }

    /**
     * 获取用户信息
     *
     * @param username
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    /**
     * 登录后获取token
     *
     * @param username
     * @param password
     */
    @Override
    public String login(String username, String password) {
        return null;
    }

    /**
     * 刷新token
     *
     * @param token
     */
    @Override
    public String refreshToken(String token) {
        return null;
    }
}
