package io.ourfit.api.domain.user.controller;

import io.ourfit.api.domain.user.data.dto.request.*;
import io.ourfit.api.domain.user.data.dto.response.UserInfoResponse;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  /** 메이트 관련 사용자 목록 조회 */
  @GetMapping("/mates")
  public ResponseEntity<BaseResponse<Page<UserInfoResponse>>> getUsers(
      UserSearchRequest request,
      Pageable pageable,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    Page<UserInfoResponse> response =
        this.userService
            .findMateCandidates(request.toDto(userDetails.getUser()), pageable)
            .map(UserInfoResponse::fromBasic);

    return ResponseEntity.ok(BaseResponse.of(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BaseResponse<UserInfoResponse>> getUser(@PathVariable final long id) {
    UserInfoResponse userInfoResponse =
        this.userService
            .findByIdWithFavorites(id)
            .map(UserInfoResponse::fromDetailed)
            .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));

    return ResponseEntity.ok(BaseResponse.of(userInfoResponse));
  }

  /** 내 정보 조회 */
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<UserInfoResponse>> getMe(
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    return this.getUser(userDetails.getId());
  }

  /** 내 기본 정보(닉네임, 나이, 지역, 운동 실력 등) 수정 */
  @PatchMapping("/me/basic-info")
  public ResponseEntity<BaseResponse<Void>> updateMyBasicInfo(
      @AuthenticationPrincipal OurfitUserDetails userDetails,
      @RequestBody @Valid UserBasicInfoUpdateRequest request) {
    this.userService.updateBasicInfo(userDetails.getId(), request.toDto());
    return ResponseEntity.ok().build();
  }

  /** 프로필(자기소개, 오픈 채팅 링크) 등록/수정 */
  @PutMapping("/me/profile")
  public ResponseEntity<BaseResponse<Void>> updateMyProfile(
      @AuthenticationPrincipal OurfitUserDetails userDetails,
      @RequestBody @Valid UserProfileUpdateRequest request) {
    this.userService.updateProfile(userDetails.getId(), request.toDto());
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
  @PutMapping("/me/workout-preferences")
  public ResponseEntity<BaseResponse<Void>> updateMyWorkoutPreferences(
      @AuthenticationPrincipal OurfitUserDetails userDetails,
      @RequestBody @Valid UserWorkoutPreferencesUpdateRequest request) {
    this.userService.updateWorkoutPreferences(userDetails.getId(), request.toDto());
    return ResponseEntity.ok().build();
  }

  /** 회원 가입 */
  @PublicApi
  @PostMapping
  public ResponseEntity<Void> create(@RequestBody @Valid UserSignUpRequest request) {
    this.userService.save(request.toDto());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** 회원 탈퇴 */
  @DeleteMapping("/me")
  public ResponseEntity<Void> delete(@AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.userService.delete(userDetails.getId());
    return ResponseEntity.noContent().build();
  }
}
