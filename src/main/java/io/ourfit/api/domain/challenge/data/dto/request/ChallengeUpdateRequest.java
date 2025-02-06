package io.ourfit.api.domain.challenge.data.dto.request;

import io.ourfit.api.global.web.validation.Enumerable;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * 챌린지 수정 요청 DTO
 *
 * @param goalDayOfWeeks 수정할 매주 목표 운동 요일
 */
public record ChallengeUpdateRequest(
    @Enumerable(type = DayOfWeek.class) Set<String> goalDayOfWeeks) {}
