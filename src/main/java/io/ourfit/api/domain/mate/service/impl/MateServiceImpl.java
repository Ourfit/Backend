package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.service.MateService;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.domain.workout.enums.MateStatusType;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.MateQRepository;
import io.ourfit.api.infra.persistence.MateRepository;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MateServiceImpl implements MateService {

  private final MateRepository repository;
  private final MateQRepository qRepository;
  private final MateWorkoutService workoutService;
  private final UserService userService;

  @Override
  public void apply(final long meId, final long receiverId) {
    User me =
        this.userService
            .findById(meId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    User mate =
        this.userService
            .findById(receiverId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    if (this.qRepository.existsPendingRequestBetweenUsers(me, mate)) {
      throw new DuplicatedException(ApiExceptionType.RESOURCE_IDENTICAL);
    }

    this.repository.save(Mate.of(me, mate));
  }

  @Override
  public void accept(final long meId, final long mateId) {
    this.ifFoundThen(
        mateId,
        mate -> {
          mate.accept();
          this.workoutService.initialize(mate);
        },
        mate -> mate.canAccept(meId));
  }

  @Override
  public void unmate(final long meId, final long mateId) {
    this.ifFoundThen(mateId, Mate::unmate, mate -> mate.canUnmate(meId));
  }

  @Override
  public Optional<Mate> findByIdAndStatus(long mateId, MateStatusType statusType) {
    return Optional.empty();
  }

  private void ifFoundThen(
      final long id, Consumer<Mate> presentAction, Predicate<Mate>... filters) {
    this.repository
        .findById(id)
        .filter(mate -> Arrays.stream(filters).allMatch(filter -> filter.test(mate)))
        .ifPresentOrElse(
            presentAction,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }

  private void ifFoundThen(
      final long id,
      Consumer<Mate> presentAction,
      Supplier<RuntimeException> exceptionSupplier,
      Predicate<Mate>... filters) {
    this.repository
        .findById(id)
        .map(mate -> doFilters(mate, exceptionSupplier, filters))
        .ifPresentOrElse(
            presentAction,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }

  private static Mate doFilters(
      Mate mate, Supplier<RuntimeException> exSupplier, Predicate<Mate>... filters) {
    if (Arrays.stream(filters).anyMatch(filter -> !filter.test(mate))) {
      throw exSupplier.get();
    }
    return mate;
  }
}
