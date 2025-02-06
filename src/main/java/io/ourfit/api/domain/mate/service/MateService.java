package io.ourfit.api.domain.mate.service;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.workout.enums.MateStatusType;
import java.util.Optional;

public interface MateService {

  /**
   * 메이트 신청
   *
   * @param meId 신청하는 사용자 ID (나)
   * @param receiverId 신청 받는 사용자 ID
   */
  void apply(long meId, long receiverId);

  /**
   * 메이트 수락
   *
   * @param meId 수락하는 사용자 ID (신청받은 사용자)
   * @param mateId 메이트 ID
   */
  void accept(long meId, long mateId);

  /**
   * 메이트 해제
   *
   * @param meId 해제를 수행하는 사용자 ID
   * @param mateId 메이트 ID
   */
  void unmate(long meId, long mateId);

  Optional<Mate> findByIdAndStatus(long mateId, MateStatusType statusType);
}
