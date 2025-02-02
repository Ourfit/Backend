package io.ourfit.api.domain.user.controller;

import io.ourfit.api.domain.user.dto.request.UserBasicInfoUpdateRequest;
import io.ourfit.api.domain.user.dto.request.UserSignUpRequest;
import io.ourfit.api.domain.user.dto.request.UserWorkoutPreferencesUpdateRequest;
import io.ourfit.api.domain.user.dto.response.UserInfoResponse;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;

  /** 내 정보 조회 */
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserInfoResponse>> getMe(
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    UserInfoResponse userInfoResponse =
        this.userService
            .findByIdWithFavorites(userDetails.getId())
            .map(UserInfoResponse::from)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    return ResponseEntity.ok(BaseResponse.of(userInfoResponse));
  }

  /** 내 기본 정보(닉네임, 나이, 지역, 운동 실력 등) 수정 */
  @PatchMapping("/me/basic")
  public ResponseEntity<BaseResponse<Void>> updateMyBasicInfo(
      @AuthenticationPrincipal OurfitUserDetails userDetails,
      @RequestBody @Valid UserBasicInfoUpdateRequest request) {
    this.userService.updateBasicInfo(userDetails.getId(), request.toDto());
    return ResponseEntity.ok().build();
  }

  /** 프로필 이미지 등록/수정 */
  @PutMapping("/me/profile-image")
  public ResponseEntity<BaseResponse<Void>> updateMyProfileImage(
      @AuthenticationPrincipal OurfitUserDetails userDetails,
      @RequestPart("profileImage") MultipartFile profileImage) {
    this.userService.updateProfileImage(userDetails.getId(), profileImage);
    return ResponseEntity.ok().build();
  }

  /** 내 운동 선호 정보 수정 */
  @PatchMapping("/me/workout-preferences")
  public ResponseEntity<BaseResponse<Void>> updateMyWorkoutPreferences(
      @AuthenticationPrincipal OurfitUserDetails userDetails,
      @RequestBody @Valid UserWorkoutPreferencesUpdateRequest request) {
    this.userService.updateWorkoutPreferences(userDetails.getId(), request.toDto());
    return ResponseEntity.ok().build();
  }

  /** 회원 가입 */
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid UserSignUpRequest request) {
    this.userService.save(request.toDto());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
