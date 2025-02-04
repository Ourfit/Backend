package io.ourfit.api.global.exception;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import io.ourfit.api.global.data.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/** 전역 예외 처리 클래스 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ignored) {
    return ResponseEntity.internalServerError().body(ErrorResponse.internalServerError());
  }

  @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
  protected ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
    return ResponseEntity.badRequest().body(ErrorResponse.badRequest(ex.getMessage()));
  }

  @ExceptionHandler(OurfitApiException.class)
  protected ResponseEntity<ErrorResponse> handleLocatApiException(OurfitApiException ex) {
    return ResponseEntity.status(ex.getHttpStatus()).body(ErrorResponse.fromException(ex));
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
        """
				Unsupported HTTP Methods!
				[Requested: %s, Supported: %s]
				"""
            .formatted(ex.getMethod(), supportedMethods);
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
      NoHandlerFoundException ignored) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.notFound("The API endpoint does not exist."));
  }
}
