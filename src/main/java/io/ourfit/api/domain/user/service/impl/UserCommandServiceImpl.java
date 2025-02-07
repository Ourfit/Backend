package io.ourfit.api.domain.user.service.impl;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.user.data.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.data.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.service.UserCommandService;
import io.ourfit.api.domain.workout.service.WorkoutService;
import io.ourfit.api.global.data.EntityFinder;
import io.ourfit.api.infra.aws.s3.OurfitS3Client;
import io.ourfit.api.infra.persistence.UserRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

  private final UserRepository repository;
  private final EntityFinder<User, Long> userEntityFinder;
  private final OAuth2Service oAuth2Service;
  private final WorkoutService workoutService;
  private final OurfitS3Client s3Client;

  @Override
  public void save(UserSignUpDto signUpDto) {
    OAuth2UserInfo oAuth2UserInfo = this.oAuth2Service.getUserInfo(signUpDto.oAuthId());
    final User user = this.repository.save(User.of(oAuth2UserInfo, signUpDto));
    // 사용자 등록 후, 선호 운동 종목 등록
    List<UserFavoriteWorkout> favoriteWorkouts =
        this.buildFavoriteWorkouts(user, signUpDto.favoriteWorkouts());
    user.setFavoriteWorkouts(favoriteWorkouts);
  }

  @Override
  public void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto) {
    this.userEntityFinder.ifFoundThen(id, user -> user.updateBasicInfo(updateDto));
  }

  @Override
  public void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto) {
    this.userEntityFinder.ifFoundThen(
        id,
        user -> {
          user.updateWorkoutPreferences(updateDto);

          if (!updateDto.favoriteWorkouts().isEmpty()) {
            List<UserFavoriteWorkout> favoriteWorkouts =
                this.buildFavoriteWorkouts(user, updateDto.favoriteWorkouts());
            user.setFavoriteWorkouts(favoriteWorkouts);
          }
        });
  }

  @Override
  public void updateProfile(long id, UserProfileUpdateDto updateDto) {
    this.userEntityFinder.ifFoundThen(id, user -> user.setProfile(updateDto));
  }

  @Override
  public void updateProfileImage(long id, MultipartFile profileImage) {
    this.userEntityFinder.ifFoundThen(
        id,
        user -> {
          final String profileImageUrl = this.s3Client.upload("버킷/이미지/경로", profileImage);
          user.setProfileImageUrl(profileImageUrl);
        });
  }

  @Override
  public void delete(long id) {
    this.userEntityFinder.ifFoundThen(
        id,
        user -> {
          this.oAuth2Service.withdrawal(user.getOAuthId());
          user.delete();
        });
  }

  private List<UserFavoriteWorkout> buildFavoriteWorkouts(User user, Set<String> favoriteWorkouts) {
    return this.workoutService.findAllByCodeIn(favoriteWorkouts).stream()
        .map(workout -> UserFavoriteWorkout.of(user, workout))
        .toList();
  }
}
