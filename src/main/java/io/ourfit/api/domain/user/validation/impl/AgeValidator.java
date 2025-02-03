package io.ourfit.api.domain.user.validation.impl;

import io.ourfit.api.domain.user.validation.Age;
import io.ourfit.api.global.validation.AbstractConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator extends AbstractConstraintValidator<Age, Number> {

  private static final int MIN_AGE = 15;
  private static final int MAX_AGE = 70;

  @Override
  public void initialize(Age constraintAnnotation) {
    this.isRequired = constraintAnnotation.required();
  }

  @Override
  public boolean isValidInternal(Number value, ConstraintValidatorContext context) {
    final int age = value.intValue();
    return age >= MIN_AGE && age <= MAX_AGE;
  }
}
