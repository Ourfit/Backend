package io.ourfit.api.domain.challenge.data.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ourfit.api.global.web.validation.Enumerable;
import jakarta.validation.constraints.Positive;
import java.time.DayOfWeek;
import java.util.Set;

/**
 * 챌린지 생성 요청 DTO
 *
 * @param mateId 메이트 ID
 * @param goalWorkoutCount 목표 매주 운동 횟수
 * @param goalWorkoutDayOfWeeks 목표 운동 요일
 * @param challengeDurationInMoths 챌린지 기간(월)
 * @param startAt 시작일
 * @param endAt 종료일
 */
public record NewChallengeRequest(
    long mateId,
    @Positive short goalWorkoutCount,
    @Enumerable(type = DayOfWeek.class) Set<String> goalWorkoutDayOfWeeks,
    @Positive short challengeDurationInMoths,
    @JsonFormat(pattern = "yyyy-MM-dd") String startAt,
    @JsonFormat(pattern = "yyyy-MM-dd") String endAt) {

  public boolean isGoalSettingInValid() {
    return this.goalWorkoutCount != this.goalWorkoutDayOfWeeks.size();
  }
}
