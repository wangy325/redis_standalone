package com.zc.redis.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zc.redis.RedisElement;
import com.zc.redis.encrypt.AesUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RedisElement element;

    @SuppressWarnings("all")
    public RedisElement getKeyValuePair() throws Exception {
        element.setHostName(env.getProperty("redis.hostName"));
        element.setPassword(AesUtil.decrypt(env.getProperty("redis.password")));
        element.setDatabase(env.getProperty("redis.database", int.class));
        element.setMaxIdle(env.getProperty("jedis.pool.maxIdle", int.class));
        element.setMinIdle(env.getProperty("jedis.pool.minIdle", int.class));
        element.setMaxWait(env.getProperty("jedis.pool.maxWait", long.class));
        element.setTestOnBorrow(env.getProperty("jedis.pool.testOnBorrow", boolean.class));
        return element;
    }

    @PostConstruct
    public void initPlaceholder() throws Exception {
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
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration rsc,
                                                         JedisPoolConfig jpc) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(rsc);
        jedisConnectionFactory.setPoolConfig(jpc);
        return jedisConnectionFactory;
    }

    /**
     * 配置RedisTemplate以及序列化规则
     *
     * @param jcf JedisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jcf) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        StringRedisSerializer src = new StringRedisSerializer();
        // 使用双括号语法
        GenericJackson2JsonRedisSerializer jrs = new GenericJackson2JsonRedisSerializer(new ObjectMapper() {{
            setSerializationInclusion(JsonInclude.Include.NON_NULL);
            setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            enableDefaultTyping(DefaultTyping.NON_FINAL);
            // default true
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
            configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        }});
        template.setConnectionFactory(jcf);
        template.setStringSerializer(src);
        template.setKeySerializer(src);
        template.setHashKeySerializer(src);
        template.setValueSerializer(jrs);
        template.setHashValueSerializer(jrs);
        return template;
    }

}
