package io.ourfit.api.domain.challenge.data.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ourfit.api.global.web.validation.Enumerable;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * 챌린지 생성 요청 DTO
 *
 * @param mateId 메이트 ID
 * @param goalWorkoutCount 목표 매주 운동 횟수
 * @param goalWorkoutDayOfWeeks 목표 운동 요일
 * @param challengeDurationInMonths 챌린지 기간(월)
 * @param startAt 시작일
 * @param endAt 종료일
 */
public record ChallengeCreateRequest(
    long mateId,
    @Positive short goalWorkoutCount,
    @Enumerable(type = DayOfWeek.class) Set<String> goalWorkoutDayOfWeeks,
    @Positive short challengeDurationInMonths,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endAt) {

  public boolean isGoalSettingInValid() {
    return (this.goalWorkoutDayOfWeeks == null
            || this.goalWorkoutCount != this.goalWorkoutDayOfWeeks.size())
        || this.startAt.isAfter(this.endAt)
        || this.challengeDurationInMonths
            != Math.max(1, this.startAt.until(this.endAt).toTotalMonths());
  }
}
