package com.druid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 1115 on 2016/10/25.
 */
@Component("redisConfig")
public class ConfigInfo {
    @Value("${redis.host}")
    private String redisUrl;

    public String getRedisUrl() {
        return redisUrl;
    }
}
