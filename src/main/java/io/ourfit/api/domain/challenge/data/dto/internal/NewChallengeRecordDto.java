package io.ourfit.api.domain.challenge.data.dto.internal;

import io.ourfit.api.domain.challenge.data.dto.request.NewChallengeRecordRequest;

/**
 * 챌린지 도전 기록 생성 DTO
 *
 * @param intensityLevel 오늘의 운동 기록 강도
 */
public record NewChallengeRecordDto(short intensityLevel) {

  public static NewChallengeRecordDto from(NewChallengeRecordRequest request) {
    return new NewChallengeRecordDto(request.intensityLevel());
  }
}
