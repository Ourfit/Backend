package io.ourfit.api.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.ourfit.api.global.exception.custom.InternalProcessingException;

public final class XmlUtils {

  private static final XmlMapper MAPPER;

  static {
    XmlMapper mapper = new XmlMapper();
    mapper.registerModule(new JavaTimeModule());
    MAPPER = mapper;
  }

  private XmlUtils() {}

  public static <T> T readValue(String content, Class<T> valueType) {
    try {
      return MAPPER.readValue(content, valueType);
    } catch (JsonProcessingException e) {
      throw new InternalProcessingException(e);
    }
  }
}
