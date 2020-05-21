package com.zc.redis.ops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 15:33
 */
@Service
public class Ops4Hash {
    private static final long EXPIRE_TIME = 24 * 3600;

    @Autowired
    private RedisTemplate<String, Object> template;

    /**
     * cache hash into redis with key as {@code key}, hash field as {@code hashKey}, value as {@code value}.
     * note {@code value} could be a string or self definition object.
     * the hash will expire in default timeout(1 day).
     *
     * @param key     string key not null
     * @param hashKey string hashKey not null
     * @param value   javabean
     */
    public <T> void hset(String key, String hashKey, T value) {
        hset(key, hashKey, value, EXPIRE_TIME);
    }

    /**
     * cache hash into redis with particular timeout.
     *
     * @param key     string key not null
     * @param hashKey string hashKey not null
     * @param value   Object
     * @param timeout unit second
     */
    public <T> void hset(String key, String hashKey, T value, long timeout) {
        BoundHashOperations<String, Object, Object> bho = getBoundHashOps(key);
        bho.put(hashKey, value);
        bho.expire(timeout, TimeUnit.SECONDS);
    }

    /**
     * insert batch hash into redis with default timeout.
     *
     * @param key string key not null
     * @param map not null
     */
    public <K, V> void hsetAll(String key, Map<K, V> map) {
        hsetAll(key, map, EXPIRE_TIME);
    }

    /**
     * insert batch hash into redis with particular timeout.
     *
     * @param key     string not null
     * @param map     not null
     * @param timeout long, unit second
     */
    public <K, V> void hsetAll(String key, Map<K, V> map, long timeout) {
        BoundHashOperations<String, Object, Object> bho = getBoundHashOps(key);
        bho.putAll(map);
        bho.expire(timeout, TimeUnit.SECONDS);
        /*template.opsForHash().putAll(key, map);
        expire(key, timeout);*/
    }

    /**
     * get a hash with key and hashKey.
     *
     * @param key     string key not null
     * @param hashKey string hash key not null
     * @param clazz   the value's class type
     * @return null if key or hashKey does not exist.
     */
    public <T> T hget(String key, String hashKey, Class<T> clazz) {
        Object o = template.opsForHash().get(key, hashKey);
        return (null == o ? null : clazz.cast(o));
    }

    /**
     * del a hash key-value pair.
     *
     * @param key     string key not null
     * @param hashKey string hashKey not null
     * @return true if hashKey was delete, false if hashKey does not exist
     */
    public boolean hdel(String key, String hashKey) {
        Long delete = template.opsForHash().delete(key, hashKey);
        return delete == 1;
    }

    /**
     * del all hash key-value pairs which keys in {@code hkl}
     *
     * @param key string key not null
     * @param hkl not null
     * @return true if all hashKey deleted
     */
    public boolean mdel(String key, List<String> hkl) {
        Long delete = template.opsForHash().delete(key, hkl.toArray());
        return delete == hkl.size();
    }

    /**
     * Get all hash key-value pairs of a particular key.<br>
     * Unless you know the type of hashKey and value clearly, or you should not use this to fetch all the content in hash.<br>
     * This method could be confusion.
     *
     * @param key string key not null
     * @return null if key does not exist
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getHash(String key) {
        return (Map<K, V>) template.opsForHash().entries(key);
    }

    /**
     * determine if given hash hashKey exists.
     *
     * @param key     string key not null
     * @param hashKey hashKey not null
     * @return true if exists
     */
    public Boolean hasKey(String key, String hashKey) {
        return template.opsForHash().hasKey(key, hashKey);
    }

    /**
     * set timeout of a hash.
     *
     * @param key string key not null
     * @param ttl timeout seconds
     * @return true if success, false otherwise. null when used in pipeline / transaction.
     */
    private boolean expire(String key, long ttl) {
        Boolean expire = template.boundHashOps(key).expire(ttl, TimeUnit.SECONDS);
        BoundHashOperations<String, Object, Object> boundHashOps = template.boundHashOps(key);
        return (null == expire ? false : expire);
    }

    public <HK, HV> BoundHashOperations<String, HK, HV> getBoundHashOps(String key) {
        return template.boundHashOps(key);
    }
}
