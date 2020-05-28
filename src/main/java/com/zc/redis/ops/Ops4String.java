package com.zc.redis.ops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 15:02
 */
@Service
public class Ops4String {
    private static final long EXPIRE_TIME = 24 * 3600;

    // TODO: 是否需要处理异常特殊情况的异常？

    @Autowired
    private RedisTemplate<String, Object> template;

    /**
     * cache simple string key->object value match into redis with default expire time.
     *
     * @param key   string key not null
     * @param value Object not null
     * @see ValueOperations
     */
    public <T> void set(@NonNull String key, @NonNull T value) {
        set(key, value, EXPIRE_TIME);
    }

    /**
     * cached simple key-value with expire time, time unit is second.
     *
     * @param key    String key not null
     * @param value  Object not null
     * @param expire expired in {@code expire} seconds
     */
    public <T> void set(@NonNull String key, @NonNull T value, long expire) {
        template.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * get a String value of particular key  from redis.
     *
     * @param key string key not null
     * @return null when key is not exist
     */
    public String get(@NonNull String key) throws JedisConnectionException {
        return ((String) template.opsForValue().get(key));
    }

    /**
     * return the object value cached by {@code set()},
     * note that it returns the object directly not the serialized json string.
     * the return value should be cast to correct class type you need.
     * and it can be cast to.
     *
     * @param key string key not null
     * @return Object or null if key does not exist
     */
    public Object getObject(@NonNull String key) {
        return (template.opsForValue().get(key));
    }

    /**
     * return the object value cached by {@code set()},
     * note that it returns the object directly not the serialized json string.
     *
     * @param key   string key not null
     * @param clazz the class type of particular Object
     * @return Object or null if key does not exist
     */
    public <T> T get(@NonNull String key, Class<T> clazz) {
        if (hasKey(key)) {
            Object o = template.opsForValue().get(key);
            return (null == o ? null : clazz.cast(o));
        }
        return null;
    }

    /**
     * increment key by 1
     *
     * @param key String key not null
     * @return long  value after increment, or null if in pipeline or transaction
     */
    public Long increment(@NonNull String key) {
        return template.opsForValue().increment(key);
    }

    /**
     * decrement key by 1
     *
     * @param key String key not null
     * @return value after decrement, or null if in pipeline or transaction
     */
    public Long decrement(@NonNull String key) {
        return template.opsForValue().decrement(key);
    }

    /**
     * @param key must not be null
     * @return true if the key was removed, false if key does not exist, throw exception when key is null
     */
    public Boolean del(@NonNull String key) {
        return template.delete(key);
    }

    /**
     * @param key must not be null
     * @return true if the key exists
     */
    public Boolean hasKey(@NonNull String key) {
        return template.hasKey(key);
    }
}
