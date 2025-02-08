package io.ourfit.api.domain.user.validation.impl;

import io.ourfit.api.domain.user.validation.Nickname;
import io.ourfit.api.global.web.validation.AbstractConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NicknameValidator extends AbstractConstraintValidator<Nickname, String> {

  public static final Pattern NICKNAME_PATTERN = Pattern.compile("^[가-힣]{1,12}$");

  @Override
  public void initialize(Nickname constraintAnnotation) {
    this.isRequired = constraintAnnotation.required();
  }

  @Override
  public boolean isValidInternal(String value, ConstraintValidatorContext context) {
    if (value.isBlank()) {
      return false;
    }
    return NICKNAME_PATTERN.matcher(value).matches();
  }
}
