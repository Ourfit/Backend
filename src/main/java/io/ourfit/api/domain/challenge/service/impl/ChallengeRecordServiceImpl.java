package io.ourfit.api.domain.challenge.service.impl;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeRecordAsDoneDto;
import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import io.ourfit.api.domain.challenge.service.ChallengeRecordService;
import io.ourfit.api.domain.challenge.service.ChallengeService;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.IllegalEntityStateException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.challenge.ChallengeRecordRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
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
  public void markAsDone(
      final long challengeId, final long userId, ChallengeRecordAsDoneDto recordDto) {
    var challenger =
        this.userQueryService
            .findById(userId)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    this.challengeService
        .findById(challengeId)
        .flatMap(
            challenge -> this.repository.findByChallengeAndRecordDate(challenge, LocalDate.now()))
        .map(
            challengeRecord ->
                doFilters(
                    challengeRecord,
                    Stream.of(
                        ChallengeRecord::isChallengeDay,
                        ChallengeRecord::isNotDone,
                        r -> r.getChallenge().isOwner(challenger))))
        .ifPresentOrElse(
            challenge -> challenge.markAsDone(recordDto),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND);
            });
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChallengeRecord> findMonthlyRecords(long challengeId, YearMonth yearMonth) {
    return this.challengeService
        .findById(challengeId)
        .map(
            challenge ->
                this.repository.findAllByChallengeAndRecordDateBetween(
                    challenge, yearMonth.atDay(1), yearMonth.atEndOfMonth()))
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND));
  }

  private static ChallengeRecord doFilters(
      ChallengeRecord entity, Stream<Predicate<ChallengeRecord>> filters) {
    if (!filters.allMatch(filter -> filter.test(entity))) {
      throw new IllegalEntityStateException();
    }
    return entity;
  }
}
