package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.MateLog;
import io.ourfit.api.domain.mate.service.MateService;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.MateLogRepository;
import io.ourfit.api.infra.persistence.MateQRepository;
import io.ourfit.api.infra.persistence.MateRepository;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MateServiceImpl implements MateService {

  private final MateRepository repository;
  private final MateQRepository qRepository;
  private final MateLogRepository logRepository;
  private final MateWorkoutService workoutService;
  private final UserService userService;

  @Override
  public void apply(final long requesterId, final long requesteeId) {
    User requester =
        this.userService
            .findById(requesterId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    User requestee =
        this.userService
            .findById(requesteeId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    if (this.qRepository.existsPendingRequestBetweenUsers(requester, requestee)) {
      throw new DuplicatedException(ApiExceptionType.RESOURCE_IDENTICAL);
    }

    Mate mate = this.repository.save(Mate.of(requester, requestee));
    this.logRepository.save(MateLog.fromEntity(mate));
  }

  @Override
  public void accept(final long mateId) {
    this.executeIfPresent(
        mateId,
        mate -> {
          mate.accept();
          this.workoutService.initialize(mate);
        });
  }

  @Override
  public void delete(final long mateId) {
    this.executeIfPresent(mateId, Mate::delete);
  }

  private void executeIfPresent(final long id, Consumer<Mate> presentAction) {
    this.repository
        .findById(id)
        .ifPresentOrElse(
            presentAction,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }
}
