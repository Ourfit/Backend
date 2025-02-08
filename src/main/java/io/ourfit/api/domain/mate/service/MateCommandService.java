package io.ourfit.api.domain.mate.service;

public interface MateCommandService {

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
   * @param mateId 메이트 ID
   * @param meId 수락하는 사용자 ID (신청받은 사용자)
   */
  void accept(long mateId, long meId);

  /**
   * 메이트 해제
   *
   * @param mateId 메이트 ID
   * @param meId 해제를 수행하는 사용자 ID
   */
  void unmate(long mateId, long meId);
}
