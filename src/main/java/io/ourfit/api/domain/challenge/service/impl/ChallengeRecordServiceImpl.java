package io.ourfit.api.domain.challenge.service.impl;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeRecordDto;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import io.ourfit.api.domain.challenge.service.ChallengeRecordService;
import io.ourfit.api.domain.challenge.service.ChallengeService;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.ChallengeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeRecordServiceImpl implements ChallengeRecordService {

  private final ChallengeRecordRepository repository;
  private final ChallengeService challengeService;
  private final UserQueryService userQueryService;

  @Override
  public void create(
      final long challengeId, final long userId, NewChallengeRecordDto newRecordDto) {
    var challenger =
        this.userQueryService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    this.challengeService
        .findById(challengeId)
        .filter(challenge -> challenge.isOwner(challenger))
        .filter(Challenge::isWorkoutDay)
        .ifPresentOrElse(
            challenge -> this.repository.save(ChallengeRecord.of(challenge, newRecordDto)),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }
}
