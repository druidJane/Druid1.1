package com.cn.common.core.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by 1115 on 2016/10/25.
 */
public class ConfigInfo {
    @Value("#{redis.host}")
    private String redisUrl;

    public String getRedisUrl() {
        return redisUrl;
    }
}
