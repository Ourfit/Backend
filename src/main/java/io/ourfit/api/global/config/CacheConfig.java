package io.ourfit.api.global.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ourfit.api.global.data.RedisSerializable;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

  public static final String CACHE_NAME = "OURFIT::CACHE::";

  private final RedisConnectionFactory connectionFactory;

  @Bean
  public RedisCacheManager redisCacheManager() {
    return RedisCacheManager.builder()
        .transactionAware()
        .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(this.connectionFactory))
        .cacheDefaults(this.redisCacheConfiguration())
        .build();
  }

  @Bean
  protected RedisCacheConfiguration redisCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .prefixCacheNameWith(CACHE_NAME)
        .entryTtl(Duration.ofHours(1))
        .enableTimeToIdle()
        .disableCachingNullValues()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(redisCacheObjectMapper())));
  }

  protected static ObjectMapper redisCacheObjectMapper() {
    return JsonMapper.builder()
        .addMixIn(RedisSerializable.class, RedisSerializableMixin.class)
        .activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .allowIfBaseType("java.util")
                .build(),
            ObjectMapper.DefaultTyping.NON_FINAL_AND_ENUMS,
            JsonTypeInfo.As.PROPERTY)
        .build()
        .registerModule(new JavaTimeModule());
  }

  /** 캐시에 저장되는 객체들의 타입 정보를 JSON에 포함시키기 위한 Jackson Mixin 클래스 */
  @JsonTypeInfo(
      use = JsonTypeInfo.Id.CLASS,
      include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  abstract static class RedisSerializableMixin {}
}
