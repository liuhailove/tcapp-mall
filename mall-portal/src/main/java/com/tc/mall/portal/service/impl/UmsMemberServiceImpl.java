package com.tc.mall.portal.service.impl;

import com.tc.mall.common.exception.Asserts;
import com.tc.mall.mapper.UmsMemberLevelMapper;
import com.tc.mall.mapper.UmsMemberMapper;
import com.tc.mall.model.UmsMember;
import com.tc.mall.model.UmsMemberExample;
import com.tc.mall.model.UmsMemberLevel;
import com.tc.mall.model.UmsMemberLevelExample;
import com.tc.mall.portal.domain.MemberDetails;
import com.tc.mall.portal.service.UmsMemberCacheService;
import com.tc.mall.portal.service.UmsMemberService;
import com.tc.mall.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 会员服务
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class.getSimpleName());

    /**
     * 随机种子
     */
    private static final Random RANDOM = new Random();
    /**
     * 密码加密
     */
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * jwt token工具
     */
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 会员Mapper
     */
    @Resource
    private UmsMemberMapper memberMapper;

    /**
     * 会员mapper
     */
    @Resource
    private UmsMemberLevelMapper memberLevelMapper;

    /**
     * 会员缓存服务
     */
    @Resource
    private UmsMemberCacheService memberCacheService;

    /**
     * 根据用户名获取会员
     *
     * @param username
     */
    @Override
    public UmsMember getByUsername(String username) {
        UmsMember member = memberCacheService.getMember(username);
        if (member != null) {
            return member;
        }
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            member = memberList.get(0);
            memberCacheService.setMember(member);
            return member;
        }
        return null;
    }

    /**
     * 根据会员编号获取会员
     *
     * @param id 会员编号
     */
    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    /**
     * 用户注册
     *
     * @param username  用户名
     * @param password  密码
     * @param telephone 电话
     * @param authCode  验证码
     */
    @Override
    public void register(String username, String password, String telephone, String authCode) {
        // 验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            Asserts.fail("验证码错误");
        }
        // 查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        example.or(example.createCriteria().andPhoneEqualTo(telephone));
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsMembers)) {
            Asserts.fail("该用户已经存在");
        }
        // 没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPhone(telephone);
        umsMember.setPassword(passwordEncoder.encode(password));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        // 获取默认会员等级并设置
        UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UmsMemberLevel> memberLevelList = memberLevelMapper.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberMapper.insert(umsMember);
        umsMember.setPassword(null);
    }

    /**
     * 生成验证码
     *
     * @param telephone 电话号码
     */
    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        RANDOM.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 6; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        memberCacheService.setAuthCode(telephone, sb.toString());
        return sb.toString();
    }

    /**
     * 修改密码
     *
     * @param telephone 电话
     * @param password  密码
     * @param authCode  验证码
     */
    @Override
    public void updatePassword(String telephone, String password, String authCode) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andPhoneEqualTo(telephone);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(memberList)) {
            Asserts.fail("该账号不存在");
        }
        // 验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            Asserts.fail("验证码错误");
        }
        UmsMember umsMember = memberList.get(0);
        umsMember.setPassword(passwordEncoder.encode(password));
        memberMapper.updateByPrimaryKeySelective(umsMember);
        memberCacheService.delMember(umsMember.getId());
    }

    /**
     * 获取当前登录会员
     */
    @Override
    public UmsMember getCurrentMember() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
        return memberDetails.getUmsMember();
    }

    /**
     * 根据会员id修改会员积分
     *
     * @param id          会员ID
     * @param integration 积分
     */
    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(id);
        umsMember.setIntegration(integration);
        memberMapper.updateByPrimaryKeySelective(umsMember);
        memberCacheService.delMember(id);
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名称
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsMember member = getByUsername(username);
        if (member != null) {
            return new MemberDetails(member);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    /**
     * 登录后获取token
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public String login(String username, String password) {
        String token = null;
        // 密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 刷新token
     *
     * @param token 上次token
     */
    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshHeadToken(token);
    }

    /**
     * 对输入的验证码进行校验
     *
     * @param authCode  验证码
     * @param telephone 电话
     * @return 验证结果
     */
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = memberCacheService.getAuthCode(telephone);
        return authCode.equals(realAuthCode);
    }
}
