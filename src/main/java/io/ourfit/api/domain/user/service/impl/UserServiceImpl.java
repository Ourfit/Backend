package io.ourfit.api.domain.user.service.impl;

import io.ourfit.api.domain.auth.data.dto.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.user.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.user.entity.User;
import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.domain.workout.service.WorkoutService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.aws.s3.OurfitS3Client;
import io.ourfit.api.infra.persistence.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository repository;
  private final OAuth2Service oAuth2Service;
  private final WorkoutService workoutService;
  private final OurfitS3Client s3Client;

  @Override
  public void save(UserSignUpDto signUpDto) {
    OAuth2UserInfo oAuth2UserInfo =
        this.oAuth2Service.authenticate(signUpDto.providerType(), signUpDto.code());
    final User user = this.repository.save(User.of(oAuth2UserInfo, signUpDto));
    // 사용자 등록 후, 선호 운동 종목 등록
    List<UserFavoriteWorkout> favoriteWorkouts =
        this.buildFavoriteWorkouts(user, signUpDto.favoriteWorkouts());
    user.updateFavoriteWorkouts(favoriteWorkouts);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByIdWithFavorites(final long id) {
    return this.repository.findByIdWithFavorites(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByEmail(String email) {
    return this.repository.findByEmail(email);
  }

  @Override
  public void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto) {
    this.repository
        .findById(id)
        .ifPresentOrElse(
            user -> user.updateBasicInfo(updateDto),
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  @Override
  public void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto) {
    this.repository
        .findById(id)
        .ifPresentOrElse(
            user -> {
              user.updateWorkoutPreferences(updateDto);

              if (!updateDto.favoriteWorkouts().isEmpty()) {
                List<UserFavoriteWorkout> favoriteWorkouts =
                    this.buildFavoriteWorkouts(user, updateDto.favoriteWorkouts());
                user.updateFavoriteWorkouts(favoriteWorkouts);
              }
            },
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  @Override
  public void updateProfileImage(long id, MultipartFile profileImage) {
    this.repository
        .findById(id)
        .ifPresentOrElse(
            user -> {
              final String profileImageUrl = this.s3Client.upload("버킷/이미지/경로", profileImage);
              user.updateProfileImage(profileImageUrl);
            },
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }

  private List<UserFavoriteWorkout> buildFavoriteWorkouts(User user, Set<String> favoriteWorkouts) {
    return this.workoutService.findAllByCodeIn(favoriteWorkouts).stream()
        .map(workout -> UserFavoriteWorkout.of(user, workout))
        .toList();
  }
}
