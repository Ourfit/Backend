package io.ourfit.api.domain.challenge.service.impl;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeCreateDto;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.challenge.service.ChallengeService;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.mate.service.MateQueryService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.infra.persistence.challenge.ChallengeRepository;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

  private final ChallengeRepository repository;
  private final MateQueryService mateQueryService;
  private final UserQueryService userQueryService;

  @Override
  public void create(final long userId, ChallengeCreateDto challengeCreateDto) {
    var challenger =
        this.userQueryService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
    var mate =
        this.mateQueryService
            .findByIdAndStatus(challengeCreateDto.mateId(), MateStatusType.MATCHED)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_MATE));

    if (this.repository.existsByMateAndUserAndDeletedAtIsNull(mate, challenger)) {
      throw new DuplicatedException();
    }

    var challenge = this.repository.save(Challenge.of(mate, challenger, challengeCreateDto));
    challenge.createChallengeRecords();
  }

  @Override
  public void setGoalDayOfWeeks(final long challengeId, Set<DayOfWeek> goalDayOfWeeks) {
    this.ifFoundThen(
        challengeId,
        challenge -> {
          challenge.setGoalWorkoutDayOfWeek(goalDayOfWeeks);
          challenge.updatePlannedRecords(goalDayOfWeeks);
        });
  }

  @Override
  public void delete(final long challengeId) {
    this.ifFoundThen(challengeId, Challenge::delete);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Challenge> findById(final long challengeId) {
    return this.repository.findById(challengeId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Challenge> findAllByUser(User user) {
    var mate =
        this.mateQueryService.findByUserAndStatus(user, MateStatusType.MATCHED).orElseThrow();
    return this.repository.findAllByMate(mate);
  }

  @SafeVarargs
  private void ifFoundThen(long id, Consumer<Challenge> action, Predicate<Challenge>... filters) {
    this.repository
        .findById(id)
        .map(entity -> StreamUtils.applyFiltersOrThrow(entity, filters))
        .ifPresentOrElse(
            action,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_CHALLENGE);
            });
  }
}
