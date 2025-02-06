package io.ourfit.api.global.web.validation;

import io.ourfit.api.global.web.validation.impl.EnumerableCollectionValidator;
import io.ourfit.api.global.web.validation.impl.EnumerableValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumerableValidator.class, EnumerableCollectionValidator.class})
public @interface Enumerable {

  /**
   * 검증할 대상 Enum 클래스를 지정한다.
   *
   * @return Enum 클래스 정보
   */
  Class<? extends Enum<?>> type();

  /**
   * 필드 값의 필수 여부를 지정한다. {@code true}이면 해당 값이 null이어도, 검증을 통과한다.
   *
   * @return 필수 여부
   */
  boolean required() default true;

  String message() default "Invalid value for this enum type";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
