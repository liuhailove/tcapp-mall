package com.tc.mall.security.config;

import com.tc.mall.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis相关配置
 *
 * @author honggang.liu
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
