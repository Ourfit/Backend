package io.ourfit.api.domain.challenge.service;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeRecordAsDoneDto;
import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import java.time.YearMonth;
import java.util.List;

public interface ChallengeRecordService {

  void markAsDone(long challengeId, long userId, ChallengeRecordAsDoneDto recordDto);

  List<ChallengeRecord> findMonthlyRecords(long challengeId, YearMonth yearMonth);
}
