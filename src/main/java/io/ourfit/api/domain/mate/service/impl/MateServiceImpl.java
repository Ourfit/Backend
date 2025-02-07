package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.MateHistory;
import io.ourfit.api.domain.mate.service.MateService;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.domain.workout.data.enums.MateActionType;
import io.ourfit.api.domain.workout.data.enums.MateStatusType;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.MateHistoryRepository;
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
  private final MateHistoryRepository historyRepository;
  private final MateWorkoutService workoutService;
  private final UserQueryService userQueryService;

  @Override
  public void apply(final long meId, final long receiverId) {
    User me =
        this.userQueryService
            .findById(meId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    User myMate =
        this.userQueryService
            .findById(receiverId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    if (this.qRepository.existsPendingRequestBetweenUsers(me, myMate)) {
      throw new DuplicatedException(ApiExceptionType.RESOURCE_IDENTICAL);
    }

    Mate mate = this.repository.save(Mate.of(me, myMate));
    this.historyRepository.save(MateHistory.from(MateActionType.APPLY, mate));
  }

  @Override
  public void accept(final long mateId, final long meId) {
    this.ifFoundThen(
        mateId,
        mate -> {
          mate.accept();
          this.workoutService.initialize(mate);
          this.historyRepository.save(MateHistory.from(MateActionType.ACCEPT, mate));
        },
        mate -> mate.canAccept(meId));
  }

  @Override
  public void unmate(final long mateId, final long meId) {
    this.ifFoundThen(
        mateId,
        mate -> {
          mate.unmate();
          this.historyRepository.save(MateHistory.from(MateActionType.UNMATE, mate));
        },
        mate -> mate.canUnmate(meId));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Mate> findByIdAndStatus(long mateId, MateStatusType status) {
    return this.repository.findByIdAndStatusType(mateId, status);
  }

  @SafeVarargs
  private void ifFoundThen(
      final long id, Consumer<Mate> presentAction, Predicate<Mate>... filters) {
    this.repository
        .findById(id)
        .map(mate -> doFilters(mate, IllegalStateException::new, filters))
        .ifPresentOrElse(
            presentAction,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }

  @SafeVarargs
  private static Mate doFilters(
      Mate mate, Supplier<RuntimeException> exSupplier, Predicate<Mate>... filters) {
    if (Arrays.stream(filters).anyMatch(filter -> filter.test(mate))) {
      throw exSupplier.get();
    }
    return mate;
  }
}
