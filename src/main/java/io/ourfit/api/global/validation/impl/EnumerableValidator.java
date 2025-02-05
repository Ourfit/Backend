package io.ourfit.api.global.validation.impl;

import io.ourfit.api.global.validation.AbstractConstraintValidator;
import io.ourfit.api.global.validation.Enumerable;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumerableValidator extends AbstractConstraintValidator<Enumerable, String> {

  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(Enumerable constraintAnnotation) {
    this.enumClass = constraintAnnotation.type();
    this.isRequired = constraintAnnotation.required();
  }

  @Override
  public boolean isValidInternal(String value, ConstraintValidatorContext context) {
    if (value.isBlank()) {
      return false;
    }
    return Arrays.stream(this.enumClass.getEnumConstants())
        .map(Enum::name)
        .anyMatch(name -> name.equalsIgnoreCase(value));
  }
}
