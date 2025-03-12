package io.ourfit.api.domain.auth.data.entity;

import io.ourfit.api.global.utils.RandomGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.time.Duration;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("AUTH_CODE")
public class OurfitAuthCode {

  @Transient
  private static final String CHARACTERS_FOR_CODE =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

  @Transient private static final int MIN_CODE_LENGTH = 10;

  @Transient private static final int MAX_CODE_LENGTH = 20;

  @Transient private static final long TTL_SECONDS = Duration.ofMinutes(15).toSeconds();

  @Id private String id;

  private String code;

  @TimeToLive private Long timeToLive;

  public static OurfitAuthCode from(String oAuthId) {
    final int length = RandomGenerator.nextInt(MIN_CODE_LENGTH, MAX_CODE_LENGTH);
    return OurfitAuthCode.builder()
        .id(oAuthId)
        .code(RandomGenerator.nextCode(length, CHARACTERS_FOR_CODE))
        .timeToLive(TTL_SECONDS)
        .build();
  }

  public boolean doesNotMatch(String code) {
    return !this.code.equals(code);
  }
}
