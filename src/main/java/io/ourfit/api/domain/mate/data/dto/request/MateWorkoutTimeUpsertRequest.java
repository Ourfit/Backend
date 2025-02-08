package io.ourfit.api.domain.mate.data.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ourfit.api.global.web.validation.Enumerable;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * 메이트와 운동 시간 정보 수정 요청 DTO
 *
 * @param workoutDays 함께 운동하는 요일
 * @param startAt 함께 운동 시작 시간
 * @param endAt 함께 운동 종료 시간
 */
public record MateWorkoutTimeUpsertRequest(
    @Enumerable(type = DayOfWeek.class) Set<String> workoutDays,
    @NotNull @JsonFormat(pattern = "HH:mm:ss") LocalTime startAt,
    @NotNull @JsonFormat(pattern = "HH:mm:ss") LocalTime endAt) {

  public boolean isTimeSettingInValid() {
    return this.startAt.isAfter(this.endAt);
  }
}
