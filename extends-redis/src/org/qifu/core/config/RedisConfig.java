// from https://www.jianshu.com/p/fc7fad6f500d
/**
 * example:
 * @Cacheable(value = "24h" ,keyGenerator = "wiselyKeyGenerator")
 * @CacheEvict(value = "24h",key = "'sjy.notion.notion.app.service.mybatis.impl.UserServiceImpl.getById{'+#entity.id+'}'",beforeInvocation = true)
 */
package org.qifu.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	
	private final static Logger log= LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		 return new RedisCacheManager(
				 RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
				 this.getRedisCacheConfigurationWithTtl(60*60*24), // 默认策略，未配置的 key 会使用这个
				 this.getRedisCacheConfigurationMap() // 指定 key 策略
		 );
	}
	
    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //可以进行过期时间配置
        redisCacheConfigurationMap.put("30m", this.getRedisCacheConfigurationWithTtl(60*30));
        redisCacheConfigurationMap.put("1h", this.getRedisCacheConfigurationWithTtl(60*60));
        redisCacheConfigurationMap.put("24h", this.getRedisCacheConfigurationWithTtl(60*60*24));
        redisCacheConfigurationMap.put("30d", this.getRedisCacheConfigurationWithTtl(60*60*24*30));
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), DefaultTyping.NON_FINAL, As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        //自定义前缀 默认为中间两个：
        redisCacheConfiguration = redisCacheConfiguration.computePrefixWith(myKeyPrefix());

        return redisCacheConfiguration;
    }	
	
    private CacheKeyPrefix myKeyPrefix(){
        return (name) -> {
            return name +":";
        };
    }
    
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append("." + method.getName());
                if(params==null||params.length==0||params[0]==null){
                    return null;
                }
                String join = String.join("&", Arrays.stream(params).map(Object::toString).collect(Collectors.toList()));
                String format = String.format("%s{%s}", sb.toString(), join);
                //log.info("缓存key：" + format);
                return format;
            }
        };
    }    
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();//序列化为String
        //不能反序列化
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);//序列化为Json
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
        	@Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.info("redis缓存获取异常："+ key);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.info("redis缓存添加异常："+ key);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.info("redis缓存删除异常："+ key);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.info("redis缓存清理异常");
            }
        };
        return cacheErrorHandler;
    }
    
}
