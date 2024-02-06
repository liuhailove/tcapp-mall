package com.tc.mall.portal.service.impl;


import com.tc.mall.common.service.RedisService;
import com.tc.mall.mapper.UmsMemberMapper;
import com.tc.mall.model.UmsMember;
import com.tc.mall.portal.service.UmsMemberCacheService;
import com.tc.mall.security.annotation.CacheException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * UmsMemberCacheService实现类
 *
 * @author honggang.liu
 */
@Service
public class UmsMemberCacheServiceImpl implements UmsMemberCacheService {
    @Resource
    private RedisService redisService;
    @Resource
    private UmsMemberMapper memberMapper;
    @Value("${spring.redis.database}")
    private String REDIS_DATABASE;
    @Value("${spring.redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${spring.redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE;
    @Value("${spring.redis.key.member}")
    private String REDIS_KEY_MEMBER;
    @Value("${spring.redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;

    @Override
    public void delMember(Long memberId) {
        UmsMember umsMember = memberMapper.selectByPrimaryKey(memberId);
        if (umsMember != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + umsMember.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public UmsMember getMember(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + username;
        return (UmsMember) redisService.get(key);
    }

    @Override
    public void setMember(UmsMember member) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + member.getUsername();
        redisService.set(key, member, REDIS_EXPIRE);
    }

    @CacheException
    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        redisService.set(key, authCode, REDIS_EXPIRE_AUTH_CODE);
    }

    @CacheException
    @Override
    public String getAuthCode(String telephone) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_AUTH_CODE + ":" + telephone;
        return (String) redisService.get(key);
    }
}