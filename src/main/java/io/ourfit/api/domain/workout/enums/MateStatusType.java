package io.ourfit.api.domain.workout.enums;

/** 메이트 상태 타입 */
public enum MateStatusType {
  /** 메이트 요청을 보낸 상태 (대기 중) */
  PENDING,
  /** 상대가 요청을 수락하여 매칭된 상태 */
  MATCHED,
  /** 내가 요청을 취소한 상태 <i>(현재 미사용)</i> */
  CANCELED,
  /** 상대가 요청을 거절한 상태 <i>(현재 미사용)</i> */
  REJECTED,
  /** 메이트였지만, 현재는 더 이상 메이트가 아닌 상태 */
  UNMATED
}
