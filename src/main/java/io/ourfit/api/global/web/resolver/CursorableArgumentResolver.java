package io.ourfit.api.global.web.resolver;

import io.ourfit.api.global.data.Cursorable;
import io.ourfit.api.global.data.dto.CursorRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CursorableArgumentResolver implements HandlerMethodArgumentResolver {

  private static final int DEFAULT_SIZE = 20;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Cursorable.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    String lastIdStr = webRequest.getParameter("lastId");
    String sizeStr = webRequest.getParameter("size");

    Long lastId = lastIdStr != null ? Long.parseLong(lastIdStr) : null;
    int size = sizeStr != null ? Integer.parseInt(sizeStr) : DEFAULT_SIZE;
    return new CursorRequest(lastId, size);
  }
}
