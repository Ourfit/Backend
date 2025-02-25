package io.ourfit.api.global.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class QueryUtils {

  private QueryUtils() {}

  /**
   * {@link Map}의 값들을 {@link List}로 변환하여 반환한다.
   *
   * @param values 변환 대상 Map (<i>key는 무시되고, value만 변환된다.</i>)
   * @return 변환된 {@link List} ({@code null}일 경우 {@link Collections#emptyList()} 반환)
   * @param <T> 변환할 값의 타입
   */
  public static <T> List<T> toList(Map<?, T> values) {
    return values == null ? Collections.emptyList() : new ArrayList<>(values.values());
  }

  public static boolean hasNext(List<?> contents, int pageSize) {
    return contents.size() > pageSize;
  }
}
