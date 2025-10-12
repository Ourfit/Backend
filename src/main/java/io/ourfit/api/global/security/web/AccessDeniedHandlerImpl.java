package io.ourfit.api.global.security.web;

import io.ourfit.api.global.utils.ClassUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
      throws IOException {
    if (log.isWarnEnabled()) {
      Throwable cause = ex.getCause();
      if (cause != null) {
        log.warn(
            "FORBIDDEN uri='{}'; {}: {} Caused by: {}: {}",
            request.getRequestURI(),
            ClassUtils.getSimpleName(ex),
            ex.getMessage(),
            ClassUtils.getSimpleName(cause),
            cause.getMessage());
      } else {
        log.warn(
            "FORBIDDEN uri='{}'; {}: {}",
            request.getRequestURI(),
            ClassUtils.getSimpleName(ex),
            ex.getMessage());
      }
    }
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }
}
