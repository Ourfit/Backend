package io.ourfit.api.global.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class StreamUtils {

  private StreamUtils() {}

  public static <T, R> List<R> mapToList(Collection<T> tCollection, Function<T, R> converter) {
    if (tCollection == null) {
      return new ArrayList<>();
    }
    return tCollection.stream().map(converter).toList();
  }

  public static <T, R> Set<R> mapToSet(Collection<T> tCollection, Function<T, R> converter) {
    if (tCollection == null) {
      return new HashSet<>();
    }
    return tCollection.stream().map(converter).collect(Collectors.toSet());
  }
}
