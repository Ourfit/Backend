package io.ourfit.api.global.exception;

import static io.ourfit.api.global.exception.ApiExceptionType.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.ourfit.api.global.data.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/** 전역 예외 처리 클래스 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource customMessageSource;

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ignored) {
    return ResponseEntity.internalServerError()
        .body(ErrorResponse.internalServerError(this.resolveMessage(INTERNAL_SERVER_ERROR)));
  }

  @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
  protected ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
    return ResponseEntity.badRequest().body(ErrorResponse.badRequest(ex.getLocalizedMessage()));
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ignored) {
    return ResponseEntity.status(UNAUTHORIZED)
        .body(ErrorResponse.unauthorized(this.resolveMessage(ApiExceptionType.UNAUTHORIZED)));
  }

  @ExceptionHandler(OurfitApiException.class)
  protected ResponseEntity<ErrorResponse> handleLocatApiException(OurfitApiException ex) {
    return ResponseEntity.status(ex.getHttpStatus())
        .body(ErrorResponse.fromException(ex, this.resolveMessage(ex.getApiExceptionType())));
  }

  /** API Endpoint에 대해 지원하지 않는 HTTP Method를 사용했을 때 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    String supportedMethods =
        Optional.of(ex)
            .map(HttpRequestMethodNotSupportedException::getSupportedHttpMethods)
            .map(methods -> String.join(",", methods.toString()))
            .orElse("None");
    String message =
        this.customMessageSource.getMessage(
            "io.ourfit.api.exception.UNSUPPORTED_METHOD.message",
            new Object[] {ex.getMethod(), supportedMethods},
            LocaleContextHolder.getLocale());
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .header(HttpHeaders.ALLOW, supportedMethods)
        .body(ErrorResponse.methodNotAllowed(message));
  }

  /** {@link Valid} 또는 {@link Validated}로 검증된 파라미터가 유효하지 않을 때 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    String message =
        bindingResult.getFieldErrors().stream()
            .map(
                fieldError ->
                    "%s - %s.".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining());

    return ResponseEntity.badRequest().body(ErrorResponse.badRequest(message));
  }

  /** {@link Validated}로 검증된 파라미터가 유효하지 않을 때 */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    String message =
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    """
						%s - %s.
						"""
                        .formatted(violation.getPropertyPath(), violation.getMessage()))
            .collect(Collectors.joining());
    return ResponseEntity.badRequest().body(ErrorResponse.badRequest(message));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      NoHandlerFoundException ex) {
    return ResponseEntity.status(ex.getStatusCode())
        .body(ErrorResponse.notFound(this.resolveMessage(ApiExceptionType.NOT_FOUND_API_ENDPOINT)));
  }

  private String resolveMessage(ApiExceptionType exceptionType) {
    return this.customMessageSource.getMessage(
        exceptionType.getMessageKey(), null, LocaleContextHolder.getLocale());
  }
}
