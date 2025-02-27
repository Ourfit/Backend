package io.ourfit.api.global.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Aspect
@Component
@Slf4j
public class RequestLoggingAdvice {

  /** 로깅할 최대 Payload 크기 */
  private static final long MAX_LOG_PAYLOAD_SIZE = 1024 * 2L;

  /** 요청/응답 본문이 없을 경우 표시할 문자열 */
  private static final String EMPTY_BODY_PLACEHOLDER = "[empty]";

  @Around("execution(* io.ourfit.api..*.controller.*.*(..))")
  public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
    if (!log.isInfoEnabled()) {
      return joinPoint.proceed();
    }
    final long startTime = System.currentTimeMillis();

    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    HttpServletResponse response = attributes.getResponse();

    if (response == null) {
      log.warn("No response found in RequestContext. Proceeding without logging.");
      return joinPoint.proceed();
    }

    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    logRequest(request);

    Object result;
    try {
      result = joinPoint.proceed();
    } finally {
      final long elapsedTime = System.currentTimeMillis() - startTime;

      logResponse(request, responseWrapper, elapsedTime);
      responseWrapper.copyBodyToResponse();
    }

    return result;
  }

  /** 요청 정보 로깅 */
  private static void logRequest(HttpServletRequest request) {
    log.info(
        """
        [Incoming Request Received] - {} {}
        From: {}
        User-Agent: {}
        Query String: {}
        """,
        request.getMethod(),
        request.getRequestURI(),
        request.getRemoteAddr(),
        request.getHeader(HttpHeaders.USER_AGENT),
        formatQueryString(request.getQueryString()));
  }

  /** 응답 정보 로깅 */
  private static void logResponse(
      HttpServletRequest request,
      ContentCachingResponseWrapper responseWrapper,
      final long elapsedTime) {
    log.info(
        """
        [Outgoing Response Sent] - {} {}
        Status: {}
        Elapsed Time: {} ms
        Response Body: {}
        """,
        request.getMethod(),
        request.getRequestURI(),
        responseWrapper.getStatus(),
        elapsedTime,
        getResponseBody(responseWrapper));
  }

  /** Response Body 추출 */
  private static String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
    byte[] responseBody = responseWrapper.getContentAsByteArray();
    if (responseBody.length == 0) {
      return EMPTY_BODY_PLACEHOLDER;
    }
    String body = new String(responseBody, StandardCharsets.UTF_8);
    return (body.length() > MAX_LOG_PAYLOAD_SIZE)
        ? body.substring(0, (int) MAX_LOG_PAYLOAD_SIZE) + "(truncated) ..."
        : body;
  }

  private static String formatQueryString(String queryString) {
    if (queryString == null || queryString.isBlank()) {
      return EMPTY_BODY_PLACEHOLDER;
    }
    return queryString;
  }
}
