package io.ourfit.api.domain.challenge.service;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeRecordDto;

public interface ChallengeRecordService {

  void create(long challengeId, long userId, NewChallengeRecordDto intensityLevel);
}
