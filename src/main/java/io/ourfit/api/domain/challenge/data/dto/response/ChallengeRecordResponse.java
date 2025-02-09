package io.ourfit.api.domain.challenge.data.dto.response;

import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;

public record ChallengeRecordResponse(long id, boolean isCompleted, String recordDate) {

  public static ChallengeRecordResponse from(ChallengeRecord record) {
    return new ChallengeRecordResponse(
        record.getId(), record.getIsCompleted(), record.getRecordDate().toString());
  }
}
