package io.ourfit.api.global.security.data.annotation;

import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicApi {

  /** API 접근 제어 수준을 설정한다. */
  AccessLevel accessLevel() default AccessLevel.PROTECTED;

  /** API Key 검증 여부를 설정한다. */
  KeyValidation keyValidation() default KeyValidation.REQUIRED;

  /** API Key를 검증할 때 사용할 헤더 이름을 설정한다. */
  String keyHeader() default "Ourfit-Api-Key";
}
