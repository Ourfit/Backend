package io.ourfit.api.global.exception;

import io.ourfit.api.global.security.data.OurfitAuditorAware;
import io.ourfit.api.global.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

  private final OurfitAuditorAware auditorAware;

  public String compact(Throwable ex) {
    final var sb = new StringBuilder(256);

    this.appendExceptionSignature(sb, ex);
    this.appendStackTrace(sb, ex.getStackTrace(), 1);
    this.appendSingleCauseIfPresent(sb, ex);
    this.appendRequestContext(sb);

    return sb.toString();
  }

  public String detail(Throwable ex) {
    final var sb = new StringBuilder(1024);
    final int maxStackTraceCount = log.isDebugEnabled() ? TRACE_COUNT : ERROR_TRACE_COUNT;

    this.appendExceptionSignature(sb, ex);
    this.appendStackTrace(sb, ex.getStackTrace(), maxStackTraceCount);
    this.appendCauseHierarchy(sb, ex);
    this.appendRequestContext(sb);

    return sb.toString();
  }

  private void appendExceptionSignature(StringBuilder sb, Throwable ex) {
    sb.append(ClassUtils.getSimpleName(ex)).append(": ").append(sanitize(ex.getMessage()));
  }

  private void appendRequestContext(StringBuilder sb) {
    sb.append("Thread: ")
        .append(Thread.currentThread().getName())
        .append(" | Auditor: ")
        .append(this.auditorAware.getCurrentAuditor().map(String::valueOf).orElse("anonymous"));
    final var requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
      final var request = servletRequestAttributes.getRequest();
      final var queryString = request.getQueryString();
      sb.append(" | Request: ")
          .append(request.getMethod())
          .append(" ")
          .append(request.getRequestURI());
      if (queryString != null) {
        sb.append("?").append(queryString);
      }
    }
  }

  private void appendStackTrace(
      StringBuilder sb, StackTraceElement[] stackTraces, final int maxStackTraceCount) {
    final int length = Math.min(stackTraces.length, maxStackTraceCount);
    sb.append(LINE_SEPARATOR).append("At: ");
    for (int i = 0; i < length; i++) {
      if (i > 0) {
        sb.append("\t..."); // 두 번째 라인부터 들여쓰기
      }
      sb.append(stackTraces[i]);
      if (i < length - 1) {
        // 마지막이 아니라면 개행
        sb.append(LINE_SEPARATOR);
      }
    }
  }

  private void appendSingleCauseIfPresent(StringBuilder sb, Throwable ex) {
    Throwable cause = ex.getCause();
    if (cause == null) {
      // 포맷 유지를 위해 개행
      sb.append(LINE_SEPARATOR);
      return;
    }
    sb.append(LINE_SEPARATOR)
        .append("cause: ")
        .append(ClassUtils.getSimpleName(cause))
        .append(": ")
        .append(sanitize(cause.getMessage()))
        .append(LINE_SEPARATOR);
  }

  private void appendCauseHierarchy(StringBuilder sb, Throwable ex) {
    final int maxDepth = log.isDebugEnabled() ? TRACE_COUNT : ERROR_TRACE_COUNT;
    Throwable cause = ex.getCause();
    int depth = 1;

    while (cause != null && depth <= maxDepth) {
      sb.append("cause[")
          .append(depth)
          .append("]: ")
          .append(ClassUtils.getSimpleName(cause))
          .append(": ")
          .append(sanitize(cause.getMessage()));
      cause = cause.getCause();
      depth++;
      if (cause != null && depth <= maxDepth) {
        // 다음 cause가 있으면 개행
        sb.append(LINE_SEPARATOR);
      }
    }
  }

  private static String sanitize(String s) {
    if (s == null) {
      return "[NULL]";
    }
    return s.replace('\n', ' ').replace('\r', ' ').replace('"', '\'');
  }
}
