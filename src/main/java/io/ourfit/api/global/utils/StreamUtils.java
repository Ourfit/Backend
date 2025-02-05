package io.ourfit.api.global.utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class StreamUtils {

  private StreamUtils() {}

  public static <T, R> List<R> mapToList(Collection<T> tCollection, Function<T, R> converter) {
    return tCollection.stream().map(converter).toList();
  }

  public static <T, R> Set<R> mapToSet(Collection<T> tCollection, Function<T, R> converter) {
    return tCollection.stream().map(converter).collect(Collectors.toSet());
  }
}
