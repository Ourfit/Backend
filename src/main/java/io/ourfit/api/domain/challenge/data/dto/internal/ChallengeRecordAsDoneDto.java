package io.ourfit.api.domain.challenge.data.dto.internal;

import io.ourfit.api.domain.challenge.data.dto.request.ChallengeRecordAsDoneRequest;

/**
 * 오늘의 챌린지 완료 기록 DTO
 *
 * @param intensityLevel 오늘의 운동 기록 강도
 */
public record ChallengeRecordAsDoneDto(short intensityLevel) {

  public static ChallengeRecordAsDoneDto from(ChallengeRecordAsDoneRequest request) {
    return new ChallengeRecordAsDoneDto(request.intensityLevel());
  }
}
