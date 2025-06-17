package com.test.product.trading.common.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.apache.commons.lang3.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    /**
     * 获取分布式锁
     */
    public static boolean tryLock(StringRedisTemplate redisTemplate, String lockKey, String lockValue, long timeoutSeconds) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue
                    , timeoutSeconds, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(result)) {
                return true;
            }
        } catch (Throwable e) {
            log.error("RedisUtil 获取分布式锁异常", e.getMessage());
        }

        return false;
    }

    /**
     * 释放分布式锁
     */
    public static boolean unLock(StringRedisTemplate redisTemplate, String lockKey) {
        try {
            Boolean result = redisTemplate.delete(lockKey);
            if (Boolean.TRUE.equals(result)) {
                return true;
            }
        } catch (Throwable e) {
            log.error("RedisUtil 释放分布式锁异常", e.getMessage());
        }

        return false;
    }

    /**
     * 设置缓存值
     */
    public static void setCacheMap(StringRedisTemplate redisTemplate, String key, String mapKey, String mapValue) {
        try {
            redisTemplate.opsForHash().put(key,mapKey,mapValue);
        } catch (Exception e) {
            log.error("RedisUtil setCacheMap异常！ {}, {}, {}, {}", key, mapKey, mapValue, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存值
     */
    public static String getCacheMap(StringRedisTemplate redisTemplate, String key, String mapKey) {
        try {
            Object mapValue = redisTemplate.opsForHash().get(key,mapKey);
            if (ObjectUtils.isNotEmpty(mapValue)) {
                return (String)mapValue;
            }
        } catch (Exception e) {
            log.error("RedisUtil getCacheMap异常！ key={}, mapKey={}, {}", key, mapKey, e.getMessage());
            e.printStackTrace();
        }

        return "";
    }

}
