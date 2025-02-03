package io.ourfit.api.domain.user.dto.request;

import io.ourfit.api.domain.user.dto.internal.UserSearchDto;
import io.ourfit.api.domain.user.entity.User;
import io.ourfit.api.domain.user.entity.enums.GenderType;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.Set;

/**
 * 사용자 검색 요청 DTO
 *
 * @param gender 조회할 성별
 * @param peferredTimes 조회할 선호 시간대
 * @param workoutTypes 조회할 운동 종류
 */
public record UserSearchRequest(
    String gender, Set<String> peferredTimes, Set<String> workoutTypes) {

  public UserSearchDto toDto(User user) {
    return new UserSearchDto(
        user.getRegion3(),
        GenderType.valueOf(this.gender),
        StreamUtils.convert(this.peferredTimes, TimePrefrenceType::valueOf),
        this.workoutTypes);
  }
}
