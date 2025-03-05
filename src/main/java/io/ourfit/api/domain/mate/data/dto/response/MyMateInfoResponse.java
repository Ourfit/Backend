package io.ourfit.api.domain.mate.data.dto.response;

import io.ourfit.api.domain.mate.data.dto.internal.MyMateInfoDto;

/**
 * 나의 메이트 정보 응답 DTO
 *
 * @param id 메이트의 사용자 ID
 * @param profileUrl 프로필 이미지 URL
 * @param nickname 닉네임
 * @param gender 성별
 * @param age 나이
 * @param skillLevel 운동 실력
 */
public record MyMateInfoResponse(
    long id, String profileUrl, String nickname, String gender, int age, String skillLevel) {

  public static MyMateInfoResponse from(MyMateInfoDto dto) {
    return new MyMateInfoResponse(
        dto.id(),
        dto.profileUrl(),
        dto.nickname(),
        dto.gender().name(),
        dto.age(),
        dto.skillLevelType().name());
  }
}
