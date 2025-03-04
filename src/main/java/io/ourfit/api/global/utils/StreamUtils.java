package io.ourfit.api.global.utils;

import io.ourfit.api.global.exception.custom.IllegalEntityStateException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StreamUtils {

  private StreamUtils() {}

  /**
   * 컬렉션의 각 요소를 변환하여 새로운 {@link List}로 반환한다.
   *
   * @param tCollection 변환할 원본 컬렉션
   * @param converter 요소를 변환하는 함수
   * @return 변환된 리스트 (원본 컬렉션이 {@code null}이면, 빈 리스트 반환)
   * @param <T> 원본 컬렉션의 요소 타입
   * @param <R> 변환된 리스트의 요소 타입
   * @apiNote 반환된 리스트는 불변이다.
   */
  public static <T, R> List<R> mapToList(Collection<T> tCollection, Function<T, R> converter) {
    if (tCollection == null) {
      return List.of();
    }
    return tCollection.stream().map(converter).toList();
  }

  /**
   * 컬렉션의 각 요소를 변환하여 새로운 {@link Set}으로 반환한다.
   *
   * @param tCollection 변환할 원본 컬렉션
   * @param converter 요소를 변환하는 함수
   * @return 변환된 Set (원본 컬렉션이 {@code null}이면, 빈 Set 반환)
   * @param <T> 원본 컬렉션의 요소 타입
   * @param <R> 변환된 Set의 요소 타입
   * @apiNote 반환된 Set은 불변이다.
   */
  public static <T, R> Set<R> mapToSet(Collection<T> tCollection, Function<T, R> converter) {
    if (tCollection == null) {
      return Set.of();
    }
    return tCollection.stream().map(converter).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * 컬렉션의 각 요소를 변환하여 새로운 {@link HashSet}으로 반환한다.
   *
   * @param tCollection 변환할 원본 컬렉션
   * @param converter 요소를 변환하는 함수
   * @return 변환된 HashSet (원본 컬렉션이 {@code null}이면, 빈 HashSet 반환)
   * @param <T> 원본 컬렉션의 요소 타입
   * @param <R> 변환된 HashSet의 요소 타입
   * @apiNote 반환된 Set은 변경 가능(mutable) 하다.
   */
  public static <T, R> Set<R> mapToHashSet(Collection<T> tCollection, Function<T, R> converter) {
    if (tCollection == null) {
      return new HashSet<>();
    }
    return tCollection.stream().map(converter).collect(Collectors.toSet());
  }

  /**
   * 엔티티에 대해 여러 필터를 적용하여 모두 통과하면 원본 엔티티를 반환한다. <br>
   * 만약 하나라도 실패하면 지정된 예외를 던진다.
   *
   * @param entity 검증할 엔티티
   * @param supplier 예외를 제공하는 공급자 (필터 조건을 통과하지 못할 경우 발생할 예외)
   * @param filters 적용할 필터 목록
   * @param <T> 엔티티의 타입
   * @return 모든 필터를 통과한 경우 원본 엔티티 반환
   * @throws RuntimeException 하나 이상의 필터를 통과하지 못한 경우
   */
  @SafeVarargs
  public static <T> T applyFiltersOrThrow(
      T entity, Supplier<? extends RuntimeException> supplier, Predicate<T>... filters) {
    if (!Stream.of(filters).allMatch(filter -> filter.test(entity))) {
      throw supplier.get();
    }
    return entity;
  }

  /**
   * 엔티티에 대해 여러 필터를 적용하여 모두 통과하면 원본 엔티티를 반환한다. <br>
   * 만약 하나라도 실패하면 {@link IllegalEntityStateException} 예외를 던진다.
   *
   * @param entity 검증할 엔티티
   * @param filters 적용할 필터 목록
   * @param <T> 엔티티의 타입
   * @return 모든 필터를 통과한 경우 원본 엔티티 반환
   * @throws IllegalEntityStateException 하나 이상의 필터를 통과하지 못한 경우
   */
  @SafeVarargs
  public static <T> T applyFiltersOrThrow(T entity, Predicate<T>... filters) {
    return applyFiltersOrThrow(entity, IllegalEntityStateException::new, filters);
  }
}
