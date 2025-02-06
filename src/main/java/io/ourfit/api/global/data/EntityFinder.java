package io.ourfit.api.global.data;

import io.ourfit.api.global.data.entity.BaseEntity;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EntityFinder<T extends BaseEntity, ID> {

  void ifFoundThen(ID id, Consumer<T> action, Predicate<T>... filters);
}
