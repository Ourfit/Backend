package io.ourfit.api.domain.user.validation.impl;

import io.ourfit.api.domain.user.validation.OpenChatUrl;
import io.ourfit.api.global.validation.AbstractConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class OpenChatUrlValidator extends AbstractConstraintValidator<OpenChatUrl, String> {

  private static final Pattern KAKAO_OPEN_CHAT_URL_REGEX =
      Pattern.compile("^https://open.kakao.com/o/[a-zA-Z0-9]{6,}$");

  @Override
  public void initialize(OpenChatUrl constraintAnnotation) {
    this.isRequired = constraintAnnotation.required();
  }

  @Override
  protected boolean isValidInternal(String value, ConstraintValidatorContext context) {
    if (value == null || value.isBlank()) {
      return false;
    }
    return KAKAO_OPEN_CHAT_URL_REGEX.matcher(value).matches();
  }
}
