package com.zc.redis.conf;

import com.zc.redis.RedisElement;
import com.zc.redis.encrypt.AesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

/**
 * @author wangy
 * @version 1.0
 * @date 2019/12/5 / 13:47
 */

@Configuration
@ComponentScan("com.zc.redis")
@PropertySources({
        @PropertySource(value = "classpath:redistandalone-dev.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:redistandalone-prod.properties", ignoreResourceNotFound = true)
})
public class StandAloneConfig {

    @Autowired
    private Environment env;
    @Autowired
    private ObjectMap om;
    @Autowired
    private RedisElement element;

    @SuppressWarnings("all")
    public RedisElement getKeyValuePair() throws Exception {
        element.setHostName(AesUtil.decrypt(env.getProperty("redis.hostName")));
        element.setPassword(AesUtil.decrypt(env.getProperty("redis.password")));
        element.setDatabase(env.getProperty("redis.database", int.class));
        element.setMaxIdle(env.getProperty("jedis.pool.maxIdle", int.class));
        element.setMinIdle(env.getProperty("jedis.pool.minIdle", int.class));
        element.setMaxWait(env.getProperty("jedis.pool.maxWait", long.class));
        element.setTestOnBorrow(env.getProperty("jedis.pool.testOnBorrow", boolean.class));
        return element;
    }

    @PostConstruct
    public void  initPlaceholder() throws Exception {
        element = getKeyValuePair();
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(element.getMaxIdle());
        poolConfig.setMinIdle(element.getMinIdle());
        poolConfig.setMaxWaitMillis(element.getMaxWait());
        poolConfig.setTestOnBorrow(element.isTestOnBorrow());
        return poolConfig;
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration sc = new RedisStandaloneConfiguration();
        sc.setHostName(element.getHostName());
        sc.setPassword(element.getPassword());
        sc.setDatabase(element.getDatabase());
        return sc;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration rsc = redisStandaloneConfiguration();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(rsc);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig());
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        StringRedisSerializer src = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jrs = new GenericJackson2JsonRedisSerializer(om);
        template.setConnectionFactory(jedisConnectionFactory());
        template.setStringSerializer(src);
        template.setKeySerializer(src);
        template.setHashKeySerializer(src);
        template.setValueSerializer(jrs);
        template.setHashValueSerializer(jrs);
        return template;
    }

}
