package io.ourfit.api.global.exception;

import static io.ourfit.api.global.exception.ApiExceptionType.INTERNAL_SERVER_ERROR;
import static io.ourfit.api.global.exception.ApiExceptionType.INVALID_PARAMETER;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.ourfit.api.global.config.properties.EnvironmentProfileType;
import io.ourfit.api.global.data.dto.ErrorResponse;
import io.ourfit.api.global.web.hook.WebhookService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

/** 전역 예외 처리 클래스 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ExceptionLogFormatter formatter;
  private final MessageSource customMessageSource;
  private final WebhookService webhookService;
  private final EnvironmentProfileType currentProfile;

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
    this.logException(ex, true);
    return ResponseEntity.internalServerError()
        .body(ErrorResponse.internalServerError(this.resolveMessage(INTERNAL_SERVER_ERROR)));
  }

  @ExceptionHandler({
    MissingServletRequestParameterException.class,
    HandlerMethodValidationException.class,
    HttpMessageNotReadableException.class
  })
  protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(Exception ex) {
    this.logException(ex, false);
    return ResponseEntity.badRequest()
        .body(ErrorResponse.badRequest(this.resolveMessage(INVALID_PARAMETER)));
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex) {
    this.logException(ex, false);
    return ResponseEntity.status(UNAUTHORIZED)
        .body(ErrorResponse.unauthorized(this.resolveMessage(ApiExceptionType.UNAUTHORIZED)));
  }

  @ExceptionHandler(OurfitApiException.class)
  protected ResponseEntity<ErrorResponse> handleLocatApiException(OurfitApiException ex) {
    this.logException(ex, false);
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
    this.logException(ex, false);
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .header(HttpHeaders.ALLOW, supportedMethods)
        .body(ErrorResponse.methodNotAllowed(message));
  }

  /** {@link Valid} 또는 {@link Validated}로 검증된 파라미터가 유효하지 않을 때 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    "%s - %s.".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining());
    this.logException(ex, false);
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
    this.logException(ex, false);
    return ResponseEntity.badRequest().body(ErrorResponse.badRequest(message));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      NoHandlerFoundException ex) {
    this.formatter.doFormat(ex);
    return ResponseEntity.status(ex.getStatusCode())
        .body(ErrorResponse.notFound(this.resolveMessage(ApiExceptionType.NOT_FOUND_API_ENDPOINT)));
  }

  private void logException(Exception ex, boolean sendWebhook) {
    String logMessage = this.formatter.doFormat(ex);

    log.error(logMessage);
    if (this.currentProfile.isRemote() && sendWebhook) {
      this.webhookService.send(logMessage);
    }
  }

  private String resolveMessage(ApiExceptionType exceptionType) {
    return this.customMessageSource.getMessage(
        exceptionType.getMessageKey(), null, LocaleContextHolder.getLocale());
  }
}
