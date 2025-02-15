package io.ourfit.api.domain.workout.data.enums;

import jakarta.annotation.Nullable;
import java.util.Arrays;

/** 메이트 액션 타입 */
public enum MateActionType {
  /** 메이트 신청 */
  APPLY,
  /** 메이트 신청 받음 */
  RECEIVE,
  /** 메이트 수락 */
  ACCEPT,
  /** 메이트 해제 */
  UNMATE;

  @Nullable public static MateActionType findByName(String name) {
    return Arrays.stream(values()).filter(v -> v.name().equals(name)).findFirst().orElse(null);
  }
}
