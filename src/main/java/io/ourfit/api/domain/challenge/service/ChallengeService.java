package io.ourfit.api.domain.challenge.service;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeCreateDto;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

public interface ChallengeService {

  void create(long userId, ChallengeCreateDto challengeCreateDto);

  void updateGoalDayOfWeeks(long challengeId, Set<DayOfWeek> goalDayOfWeeks);

  void delete(long challengeId);

  Optional<Challenge> findById(long challengeId);

  Optional<Challenge> findByUserIdWithRecords(long challengeId);
}
