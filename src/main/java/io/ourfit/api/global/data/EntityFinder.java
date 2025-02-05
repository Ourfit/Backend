package io.ourfit.api.global.data;

import java.util.function.Consumer;

@FunctionalInterface
public interface EntityFinder<T, ID> {

  void executeIfPresent(ID id, Consumer<T> presentConsumer);
}
