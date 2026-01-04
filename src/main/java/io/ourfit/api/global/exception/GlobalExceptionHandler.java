package io.ourfit.api.global.exception;

import static io.ourfit.api.global.exception.ApiExceptionType.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import io.ourfit.api.global.config.properties.EnvironmentProfileType;
import io.ourfit.api.global.data.dto.ErrorResponse;
import io.ourfit.api.global.data.dto.FieldError;
import io.ourfit.api.global.web.hook.WebhookService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.sql.ConversionException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.ServletRequestBindingException;
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
    String message = this.formatter.detail(ex);

    if (this.currentProfile.isRemote()) {
      this.webhookService.send(message);
    }

    log.error(message);
    return ResponseEntity.internalServerError()
        .body(ErrorResponse.internalServerError(this.resolveMessage(INTERNAL_SERVER_ERROR)));
  }

  @ExceptionHandler(OurfitApiException.class)
  protected ResponseEntity<ErrorResponse> handleLocatApiException(OurfitApiException ex) {
    if (ex.is5xxServerError()) {
      log.error(this.formatter.detail(ex));
    } else {
      log.warn(this.formatter.compact(ex));
    }
    return ResponseEntity.status(ex.getHttpStatus())
        .body(ErrorResponse.fromException(ex, this.resolveMessage(ex.getApiExceptionType())));
  }

  @ExceptionHandler({
    MissingRequestValueException.class,
    HandlerMethodValidationException.class,
    HttpMessageNotReadableException.class,
    ServletRequestBindingException.class,
    TypeMismatchException.class,
    ConversionException.class
  })
  protected ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
    log.error(this.formatter.compact(ex));
    return ResponseEntity.badRequest()
        .body(ErrorResponse.badRequest(this.resolveMessage(INVALID_PARAMETER)));
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex) {
    log.error(this.formatter.compact(ex));
    return ResponseEntity.status(UNAUTHORIZED)
        .body(ErrorResponse.unauthorized(this.resolveMessage(ApiExceptionType.UNAUTHORIZED)));
  }

  /** API Endpoint에 대해 지원하지 않는 HTTP Method를 사용했을 때 */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    Set<HttpMethod> supported = ex.getSupportedHttpMethods();
    HttpMethod[] methods =
        supported != null ? supported.toArray(HttpMethod[]::new) : new HttpMethod[0];

    log.warn(this.formatter.compact(ex));
    return ResponseEntity.status(ex.getStatusCode()).allow(methods).build();
  }

  /** {@link Valid} 또는 {@link Validated}로 검증된 파라미터가 유효하지 않을 때 */
  @ExceptionHandler(BindException.class)
  protected ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
    log.info(this.formatter.compact(ex));
    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.badRequest(this.resolveMessage(INVALID_PARAMETER), FieldError.from(ex)));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.info(this.formatter.compact(ex));
    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.badRequest(this.resolveMessage(INVALID_PARAMETER), FieldError.from(ex)));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
      NoHandlerFoundException ex) {
    log.info(this.formatter.compact(ex));
    return ResponseEntity.status(ex.getStatusCode())
        .body(ErrorResponse.notFound(this.resolveMessage(ApiExceptionType.NOT_FOUND_API_ENDPOINT)));
  }

  @ExceptionHandler(HttpMediaTypeException.class)
  protected ResponseEntity<Void> handleHttpMediaTypeException(HttpMediaTypeException ex) {
    log.warn(this.formatter.compact(ex));
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.VARY, HttpHeaders.ACCEPT);
    headers.add(HttpHeaders.VARY, HttpHeaders.CONTENT_TYPE);
    if (ex instanceof HttpMediaTypeNotAcceptableException notAcceptable) {
      // 406 Not Acceptable
      headers.setContentType(
          notAcceptable.getSupportedMediaTypes().stream().findFirst().orElse(null));
    } else if (ex instanceof HttpMediaTypeNotSupportedException notSupported) {
      // 415 Unsupported Media Type
      headers.setAccept(notSupported.getSupportedMediaTypes());
    }
    return ResponseEntity.status(ex.getStatusCode()).headers(headers).build();
  }

  private String resolveMessage(ApiExceptionType exceptionType) {
    return this.customMessageSource.getMessage(
        exceptionType.getMessageKey(), null, LocaleContextHolder.getLocale());
  }
}
