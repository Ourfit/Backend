package io.ourfit.api.global.data.dto;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.Arrays;
import java.util.List;
import org.springframework.validation.BindException;

public record FieldError(String field, String reason, String rejectedValue) {

  public static List<FieldError> from(BindException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
        .map(
            error ->
                new FieldError(
                    error.getField(),
                    error.getDefaultMessage(),
                    valueToString(error.getRejectedValue())))
        .toList();
  }

  public static List<FieldError> from(ConstraintViolationException ex) {
    return ex.getConstraintViolations().stream()
        .map(
            violation ->
                new FieldError(
                    extractFieldName(violation.getPropertyPath()),
                    violation.getMessage(),
                    valueToString(violation.getInvalidValue())))
        .toList();
  }

  private static String valueToString(Object value) {
    if (value == null) {
      return null;
    }
    final var s = String.valueOf(value);
    return (s.length() > 200) ? s.substring(0, 200) + "...(truncated)" : s;
  }

  private static String extractFieldName(Path path) {
    var fullPath = path.toString();
    var parts = fullPath.split("\\.");
    return parts.length >= 2
        ? String.join(".", Arrays.copyOfRange(parts, 1, parts.length))
        : fullPath;
  }
}
