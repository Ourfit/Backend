package io.ourfit.api.infra.persistence.challenge;

import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import java.time.YearMonth;
import java.util.List;

public interface ChallengeRecordQRepository {

  List<ChallengeRecord> findMonthlyRecords(long challengeId, YearMonth yearMonth);
}
