package io.ourfit.api.global.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public final class StreamUtils {

  private StreamUtils() {}

  public static <T, R> List<R> convert(Collection<T> tCollection, Function<T, R> converter) {
    return tCollection.stream().map(converter).toList();
  }
}
