package io.ourfit.api.domain.challenge.data.dto.response;

import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;

/**
 * 챌린지 도전 기록 응답 DTO
 *
 * @param id 기록 ID
 * @param isCompleted 완료 여부
 * @param recordDate 완료 또는 예정 날짜
 */
public record ChallengeRecordResponse(long id, boolean isCompleted, String recordDate) {

  public static ChallengeRecordResponse from(ChallengeRecord challengeRecord) {
    return new ChallengeRecordResponse(
        challengeRecord.getId(),
        challengeRecord.getIsCompleted(),
        challengeRecord.getRecordDate().toString());
  }
}
