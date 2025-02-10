package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.data.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.data.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.user.data.entity.User;
import org.springframework.web.multipart.MultipartFile;

/** 사용자 정보 변경을 처리하는 인터페이스 */
public interface UserCommandService {

  /**
   * 신규 사용자를 등록한다.
   *
   * @param signUpDto 사용자 가입 정보
   * @return 등록된 사용자 정보
   */
  User save(UserSignUpDto signUpDto);

  /**
   * 사용자의 기본 정보(닉네임, 나이, 지역, 운동 실력 등)를 수정한다.
   *
   * @param id 기본 정보를 변경할 사용자의 ID
   * @param basicInfoUpdateDto 변경할 사용자 정보
   */
  void updateBasicInfo(final long id, UserBasicInfoUpdateDto basicInfoUpdateDto);

  /**
   * 사용자의 운동 선호 정보를 설정한다.
   *
   * @param id 운동 선호 정보를 다시 설정할 사용자의 ID
   * @param workoutPreferencesUpdateDto 설정할 사용자 정보
   */
  void setWorkoutPreferences(
      final long id, UserWorkoutPreferencesUpdateDto workoutPreferencesUpdateDto);

  /**
   * 사용자의 프로필 정보를 설정한다.
   *
   * @param id 프로필 정보를 다시 설정할 사용자의 ID
   * @param profileUpdateDto 설정할 사용자 정보
   */
  void setProfile(final long id, UserProfileUpdateDto profileUpdateDto);

  /**
   * 사용자의 프로필 이미지를 설정한다.
   *
   * @param id 프로필 이미지를 설정할 사용자의 ID
   * @param profileImage 설정할 프로필 이미지
   */
  void setProfileImage(final long id, MultipartFile profileImage);

  /**
   * 사용자를 탈퇴 처리한다.
   *
   * @param id 탈퇴하는 사용자의 ID
   */
  void delete(final long id);
}
