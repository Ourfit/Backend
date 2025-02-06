package io.ourfit.api.global.web.validation.impl;

import io.ourfit.api.global.web.validation.AbstractConstraintValidator;
import io.ourfit.api.global.web.validation.Enumerable;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;

public class EnumerableCollectionValidator
    extends AbstractConstraintValidator<Enumerable, Collection<String>> {

  private Class<? extends Enum<?>> enumClass;

  @Override
  public void initialize(Enumerable constraintAnnotation) {
    this.enumClass = constraintAnnotation.type();
    this.isRequired = constraintAnnotation.required();
  }

  @Override
  protected boolean isValidInternal(Collection<String> values, ConstraintValidatorContext context) {
    if (values == null || values.isEmpty()) {
      return false;
    }
    return values.stream()
        .allMatch(
            value ->
                Arrays.stream(this.enumClass.getEnumConstants())
                    .map(Enum::name)
                    .anyMatch(name -> name.equalsIgnoreCase(value)));
  }
}
