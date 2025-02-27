package io.ourfit.api.global.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ourfit.api.global.security.data.OurfitAuditorAware;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionLoggingAdvice {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  /** 줄바꿈 문자 */
  private static final String LINE_SEPARATOR = System.lineSeparator();

  /** 로그에 남길 메서드 인자 최대 길이 */
  private static final int MAX_ARGUMENT_LENGTH = 50;

  /** 디버그 모드에서 출력할 스택 트레이스 개수 */
  private static final int DEBUG_TRACE_COUNT = 10;

  /** 에러 모드에서 출력할 스택 트레이스 개수 */
  private static final int ERROR_TRACE_COUNT = 3;

  private final HttpServletRequest currentRequest;
  private final OurfitAuditorAware auditorAware;

  /**
   * 예외 발생 시 로그를 기록한다.
   *
   * <h3>로깅 처리 방식</h3>
   *
   * <ul>
   *   <li>StringBuilder로 로그 메세지를 생성한다.
   *   <li>로그 레벨(DEBUG)에 따라 추가 메타 정보를 조건부로 생성한다.
   *   <li>로그 메시지는 최종적으로 한 번만 {@code log.error}로 출력된다.
   * </ul>
   *
   * @param joinPoint 예외가 발생한 메서드에 대한 정보가 포함된 {@link JoinPoint} 객체
   * @param ex 발생한 예외 객체
   */
  @AfterThrowing(
      pointcut =
          "execution(* io.ourfit.api..*.*(..)) && !@within(org.springframework.boot.context.properties.ConfigurationProperties)",
      throwing = "ex")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
    this.doLogExceptionInternal(joinPoint, ex);
  }

  private void doLogExceptionInternal(JoinPoint joinPoint, Throwable ex) {
    StringBuilder logBuilder = new StringBuilder();
    // 1. 예외 요약
    logBuilder
        .append(LINE_SEPARATOR)
        .append("=== Exception Log - ")
        .append(ex.getClass().getSimpleName())
        .append(" ===")
        .append(LINE_SEPARATOR);
    logBuilder.append("Message: ").append(ex.getMessage()).append(LINE_SEPARATOR);
    // 2. 발생 위치
    logBuilder.append("At: ");
    appendStackTrace(logBuilder, ex);
    // 3. 스레드 정보
    Thread currentThread = Thread.currentThread();
    logBuilder
        .append("Thread: ")
        .append(currentThread.getName())
        .append(" (ID: ")
        .append(currentThread.getId())
        .append(")")
        .append(LINE_SEPARATOR);
    // 4. 메서드 정보
    Signature signature = joinPoint.getSignature();
    logBuilder
        .append("Origin: ")
        .append(signature.getDeclaringTypeName())
        .append(".")
        .append(signature.getName())
        .append(LINE_SEPARATOR);
    if (log.isDebugEnabled()) {
      logBuilder
          .append("Arguments: ")
          .append(formatArguments(joinPoint.getArgs()))
          .append(LINE_SEPARATOR);
    }
    // 5. 원인 정보
    Throwable cause = ex.getCause();
    if (cause != null) {
      logBuilder
          .append("Caused By: ")
          .append(cause.getClass().getSimpleName())
          .append(LINE_SEPARATOR)
          .append("Cause Message: ")
          .append(cause.getMessage())
          .append(LINE_SEPARATOR);
    }
    // 6. 추가 정보
    if (log.isDebugEnabled()) {
      logBuilder.append("-- Additional Information --").append(LINE_SEPARATOR);
      if (this.currentRequest != null) {
        logBuilder
            .append("Request Method: ")
            .append(this.currentRequest.getMethod())
            .append(LINE_SEPARATOR)
            .append("Request URI: ")
            .append(this.currentRequest.getRequestURI())
            .append(LINE_SEPARATOR);
        String queryString = this.currentRequest.getQueryString();
        if (queryString != null) {
          logBuilder.append("Query String: ").append(queryString).append(LINE_SEPARATOR);
        }
      }
      // 7. 요청 사용자 정보
      this.auditorAware
          .getCurrentAuditorUser()
          .ifPresentOrElse(
              user ->
                  logBuilder.append("Requested User: ").append(user.getId()).append(LINE_SEPARATOR),
              () ->
                  logBuilder
                      .append("Requested User: ")
                      .append(OurfitAuditorAware.ANONYMOUS_USER)
                      .append(LINE_SEPARATOR));
    }
    // 최종 로그 출력
    log.error(logBuilder.toString());
  }

  private static void appendStackTrace(StringBuilder logBuilder, Throwable ex) {
    StackTraceElement[] stackTraceElements = ex.getStackTrace();
    int maxStackTraceCount = resolveStackTraceCount();
    int length = Math.min(stackTraceElements.length, maxStackTraceCount);
    for (int i = 0; i < length; i++) {
      if (i > 0) {
        logBuilder.append("\t..."); // 두 번째 라인부터 들여쓰기
      }
      logBuilder.append(stackTraceElements[i]).append(LINE_SEPARATOR);
    }
  }

  private static int resolveStackTraceCount() {
    return log.isDebugEnabled() ? DEBUG_TRACE_COUNT : ERROR_TRACE_COUNT;
  }

  private static String formatArguments(Object... arguments) {
    try {
      String jsonArgs = MAPPER.writeValueAsString(arguments);
      return jsonArgs.length() > MAX_ARGUMENT_LENGTH
          ? jsonArgs.substring(0, MAX_ARGUMENT_LENGTH) + " (truncated)..."
          : jsonArgs;
    } catch (IOException ex) {
      return String.format("[Non-Serializable Arguments - Error: %s]", ex.getMessage());
    }
  }
}
