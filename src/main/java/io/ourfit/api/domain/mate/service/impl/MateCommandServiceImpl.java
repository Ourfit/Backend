package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.MateHistory;
import io.ourfit.api.domain.mate.data.enums.MateActionType;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.mate.service.MateCommandService;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.infra.persistence.mate.MateHistoryRepository;
import io.ourfit.api.infra.persistence.mate.MateQRepository;
import io.ourfit.api.infra.persistence.mate.MateRepository;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MateCommandServiceImpl implements MateCommandService {

  private final MateRepository repository;
  private final MateQRepository qRepository;
  private final MateHistoryRepository historyRepository;
  private final MateWorkoutService workoutService;
  private final UserQueryService userQueryService;

  @Override
  public void apply(final long meId, final long receiverId) {
    final var me = this.findUserById(meId);
    final var myMate = this.findUserById(receiverId);

    if (this.qRepository.hasMateWithStatus(MateStatusType.PENDING, me, myMate)) {
      throw new DuplicatedException(ApiExceptionType.RESOURCE_ALREADY_EXISTS);
    }

    final var mate = this.repository.save(Mate.of(me, myMate));
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
        mate -> mate.canAccept(meId),
        mate -> !this.qRepository.hasMatchedMateEither(mate.getMe(), mate.getMyMate()));
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

  @SafeVarargs
  private void ifFoundThen(long id, Consumer<Mate> action, Predicate<Mate>... filters) {
    this.repository
        .findById(id)
        .map(entity -> StreamUtils.applyFiltersOrThrow(entity, filters))
        .ifPresentOrElse(
            action,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_MATE);
            });
  }

  private User findUserById(long userId) {
    return this.userQueryService
        .findById(userId)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }
}
