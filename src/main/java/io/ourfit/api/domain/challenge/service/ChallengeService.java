package io.ourfit.api.domain.challenge.service;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeDto;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

public interface ChallengeService {

  void create(long userId, NewChallengeDto newChallengeDto);

  void updateGoalDayOfWeeks(long challengeId, Set<DayOfWeek> goalDayOfWeeks);

  Optional<Challenge> findById(long challengeId);
}
