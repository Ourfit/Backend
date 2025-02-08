package io.ourfit.api.domain.challenge.data.dto.request;

import org.hibernate.validator.constraints.Range;

/**
 * 챌린지 도전 기록 완료 요청 DTO
 *
 * @param intensityLevel 오늘의 운동 기록 강도
 */
public record ChallengeRecordAsDoneRequest(@Range(min = 1, max = 3) short intensityLevel) {}
