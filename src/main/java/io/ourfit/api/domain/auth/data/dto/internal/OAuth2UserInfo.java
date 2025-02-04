package io.ourfit.api.domain.auth.data.dto.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import jakarta.annotation.Nullable;

/** OAuth2 제공자로부터 받은 사용자 정보를 표준화하는 인터페이스 */
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface OAuth2UserInfo permits KakaoUserInfo {

  /**
   * 각 제공자가 발급 & 관리하는 사용자 고유 ID을 반환한다.
   *
   * @return 사용자 고유 ID
   */
  String getId();

  /**
   * 사용자 이메일 주소를 반환한다.
   *
   * @return 사용자 이메일
   */
  String getEmail();

  /**
   * 사용자의 이름을 반환한다.
   *
   * @return 사용자 이름 / OAuth2 제공자에 따라 nullable
   */
  @Nullable String getName();

  /**
   * OAuth2 제공자 타입을 반환한다.
   *
   * @return OAuth2 제공자 타입
   */
  OAuth2ProviderType getProvider();
}
