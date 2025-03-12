package io.ourfit.api.global.exception;

import io.ourfit.api.global.security.data.OurfitAuditorAware;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionLogFormatter {

  /** 줄바꿈 문자 */
  private static final String LINE_SEPARATOR = System.lineSeparator();

  /** 디버그 모드에서 출력할 스택 트레이스 개수 */
  private static final int TRACE_COUNT = 10;

  /** 에러 모드에서 출력할 스택 트레이스 개수 */
  private static final int ERROR_TRACE_COUNT = 3;

  private final HttpServletRequest currentRequest;
  private final OurfitAuditorAware auditorAware;

  public String doFormat(Throwable ex) {
    StringBuilder logBuilder = new StringBuilder();
    StackTraceElement[] stackTraces = ex.getStackTrace();
    // 예외 요약
    logBuilder
        .append(LINE_SEPARATOR)
        .append(String.format("=== Exception Log - %s ===", ex.getClass().getSimpleName()))
        .append(LINE_SEPARATOR);
    logBuilder.append("Message: ").append(ex.getMessage()).append(LINE_SEPARATOR);
    // 발생 위치
    logBuilder.append("At: ");
    appendStackTrace(logBuilder, stackTraces);
    // 스레드 정보
    Thread currentThread = Thread.currentThread();
    logBuilder
        .append("Thread: ")
        .append(currentThread.getName())
        .append(String.format(" (ID: %d)", currentThread.getId()))
        .append(LINE_SEPARATOR);
    // 원인 정보
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
    // HTTP 요청 정보
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
    // 요청 사용자 정보
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

    // 최종 로그 출력
    return logBuilder.toString();
  }

  private static void appendStackTrace(StringBuilder logBuilder, StackTraceElement[] stackTraces) {
    int maxStackTraceCount = resolveStackTraceCount();
    int length = Math.min(stackTraces.length, maxStackTraceCount);
    for (int i = 0; i < length; i++) {
      if (i > 0) {
        logBuilder.append("\t..."); // 두 번째 라인부터 들여쓰기
      }
      logBuilder.append(stackTraces[i]).append(LINE_SEPARATOR);
    }
  }

  private static int resolveStackTraceCount() {
    return log.isDebugEnabled() ? TRACE_COUNT : ERROR_TRACE_COUNT;
  }
}
