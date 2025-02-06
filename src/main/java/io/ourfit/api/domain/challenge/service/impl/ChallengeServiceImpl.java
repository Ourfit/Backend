package io.ourfit.api.domain.challenge.service.impl;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeDto;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.challenge.service.ChallengeService;
import io.ourfit.api.domain.mate.service.MateService;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.domain.workout.data.enums.MateStatusType;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.ChallengeRepository;
import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

  private final ChallengeRepository repository;
  private final MateService mateService;
  private final UserService userService;

  @Override
  public void create(final long userId, NewChallengeDto newChallengeDto) {
    var challenger =
        this.userService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    this.mateService
        .findByIdAndStatus(newChallengeDto.mateId(), MateStatusType.MATCHED)
        .ifPresentOrElse(
            mate -> this.repository.save(Challenge.of(mate, challenger, newChallengeDto)),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }

  @Override
  public void updateGoalDayOfWeeks(final long challengeId, Set<DayOfWeek> goalDayOfWeeks) {
    this.repository
        .findById(challengeId)
        .ifPresentOrElse(
            challenge -> challenge.setGoalWorkoutDayOfWeek(goalDayOfWeeks),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Challenge> findById(final long challengeId) {
    return this.repository.findById(challengeId);
  }
}
