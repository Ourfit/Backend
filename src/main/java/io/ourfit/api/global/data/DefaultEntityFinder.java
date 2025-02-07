package io.ourfit.api.global.data;

import io.ourfit.api.global.data.entity.BaseEntity;
import io.ourfit.api.global.exception.custom.IllegalEntityStateException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultEntityFinder<T extends BaseEntity, ID>
    implements EntityFinder<T, ID> {

  private final JpaRepository<T, ID> repository;

  @Override
  @SafeVarargs
  public final void ifFoundThen(ID id, Consumer<T> action, Predicate<T>... filters) {
    this.repository
        .findById(id)
        .map(entity -> this.doFilters(entity, Stream.of(filters)))
        .ifPresentOrElse(action, NoSuchEntityException::new);
  }

  @Override
  @SafeVarargs
  public final void ifFoundThenOrElse(
      ID id,
      Consumer<T> action,
      Supplier<? extends RuntimeException> orElse,
      Predicate<T>... filters) {
    this.repository
        .findById(id)
        .map(entity -> this.doFilters(entity, Stream.of(filters), orElse))
        .ifPresentOrElse(action, NoSuchEntityException::new);
  }

  private T doFilters(
      T entity,
      Stream<Predicate<T>> filters,
      Supplier<? extends RuntimeException> exceptionSupplier) {
    if (!filters.allMatch(filter -> filter.test(entity))) {
      throw exceptionSupplier.get();
    }
    return entity;
  }

  private T doFilters(T entity, Stream<Predicate<T>> filters) {
    return this.doFilters(entity, filters, IllegalEntityStateException::new);
  }
}
