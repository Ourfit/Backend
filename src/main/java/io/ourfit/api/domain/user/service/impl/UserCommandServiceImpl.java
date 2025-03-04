package io.ourfit.api.domain.user.service.impl;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.reference.service.RegionService;
import io.ourfit.api.domain.user.data.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.data.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkoutPlace;
import io.ourfit.api.domain.user.service.UserCommandService;
import io.ourfit.api.domain.workout.service.WorkoutService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.global.exception.custom.InvalidParameterException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.infra.aws.s3.OurfitS3Client;
import io.ourfit.api.infra.aws.s3.S3Utils;
import io.ourfit.api.infra.persistence.user.UserRepository;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository repository;
  private final AuthService authService;
  private final OAuth2Service oAuth2Service;
  private final WorkoutService workoutService;
  private final RegionService regionService;
  private final OurfitS3Client s3Client;

  @Override
  public User save(UserSignUpDto signUpDto) {
    this.authService.consumeAuthCode(signUpDto.oAuthId(), signUpDto.code());
    if (!this.regionService.isValidRegion(
        signUpDto.region1(), signUpDto.region2(), signUpDto.region3())) {
      throw new InvalidParameterException(ApiExceptionType.INVALID_REGION);
    }
    if (this.repository.existsByoAuthIdAndDeletedAtIsNull(signUpDto.oAuthId())
        || this.repository.existsByNicknameAndDeletedAtIsNull(signUpDto.nickname())) {
      throw new DuplicatedException();
    }

    OAuth2UserInfo oAuth2UserInfo = this.oAuth2Service.getUserInfo(signUpDto.oAuthId());

    // 사용자 등록 후, 선호 운동 종목 등록
    final User user = this.repository.save(User.of(oAuth2UserInfo, signUpDto));
    user.setFavoriteWorkouts(this.buildFavoriteWorkouts(user, signUpDto.favoriteWorkouts()));
    return user;
  }

  @Override
  public void updateBasicInfo(final long id, UserBasicInfoUpdateDto basicInfoUpdateDto) {
    if (basicInfoUpdateDto.nickname() != null
        && this.repository.existsByNicknameAndDeletedAtIsNull(basicInfoUpdateDto.nickname())) {
      throw new DuplicatedException();
    }

    this.ifFoundThen(id, user -> user.updateBasicInfo(basicInfoUpdateDto));
  }

  @Override
  public void setWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto) {
    this.ifFoundThen(
        id,
        user -> {
          user.setPreferredWorkoutTime(updateDto.preferredWorkoutTime());
          user.setFavoriteWorkoutPlaces(
              UserFavoriteWorkoutPlace.of(user, updateDto.favoritePlaces()));
          user.setFavoriteWorkouts(this.buildFavoriteWorkouts(user, updateDto.favoriteWorkouts()));
        });
  }

  @Override
  public void setProfile(final long id, UserProfileUpdateDto profileUpdateDto) {
    this.ifFoundThen(id, user -> user.setProfile(profileUpdateDto));
  }

  @Override
  public void setProfileImage(final long id, MultipartFile profileImage) {
    this.ifFoundThen(
        id,
        user -> {
          var oldProfileUrl = user.getProfileImageUrl();
          if (profileImage == null) {
            this.s3Client.deleteByUrl(oldProfileUrl);
            return;
          }
          if (oldProfileUrl != null) {
            this.s3Client.deleteByUrl(oldProfileUrl);
          }

          var newKey = S3Utils.createTimeBasedKey(Long.toString(user.getId()));
          final var newProfileUrl =
              this.s3Client.upload("images/users/profiles", newKey, profileImage);
          user.setProfileImageUrl(newProfileUrl);
        });
  }

  @Override
  public void delete(final long id) {
    this.ifFoundThen(
        id,
        user -> {
          var oldProfileUrl = user.getProfileImageUrl();
          if (oldProfileUrl != null) {
            this.s3Client.deleteByUrl(oldProfileUrl);
          }
          this.oAuth2Service.withdrawal(user.getOAuthId());
          user.delete();
        });
  }

  private Set<UserFavoriteWorkout> buildFavoriteWorkouts(User user, Set<String> favoriteWorkouts) {
    return this.workoutService.findAllByCodeIn(favoriteWorkouts).stream()
        .map(workout -> UserFavoriteWorkout.of(user, workout))
        .collect(Collectors.toUnmodifiableSet());
  }

  @SafeVarargs
  private void ifFoundThen(long id, Consumer<User> action, Predicate<User>... filters) {
    this.repository
        .findById(id)
        .map(entity -> StreamUtils.applyFiltersOrThrow(entity, filters))
        .ifPresentOrElse(
            action,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER);
            });
  }
}
