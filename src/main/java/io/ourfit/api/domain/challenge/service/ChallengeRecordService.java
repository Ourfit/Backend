package io.ourfit.api.domain.challenge.service;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeRecordAsDoneDto;
import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import java.time.YearMonth;
import java.util.List;

public interface ChallengeRecordService {

  /**
   * 오늘의 챌린지를 완료 처리한다.
   *
   * @param challengeId 완료 처리할 챌린지 ID
   * @param userId 챌린지에 도전하는 사용자 ID
   * @param recordDto 완료 처리할 챌린지 기록 DTO
   */
  void markAsDone(long challengeId, long userId, ChallengeRecordAsDoneDto recordDto);

  /**
   * 해당 챌린지의 해당 월의 기록을 조회한다.
   *
   * @param challengeId 조회할 챌린지 ID
   * @param yearMonth 조회할 연월
   * @return 해당 챌린지의 해당 월의 기록
   */
  List<ChallengeRecord> findMonthlyRecords(long challengeId, YearMonth yearMonth);
}
