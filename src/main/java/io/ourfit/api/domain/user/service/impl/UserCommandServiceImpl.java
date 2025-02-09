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
import io.ourfit.api.global.data.AbstractEntityFinder;
import io.ourfit.api.global.exception.custom.DuplicatedException;
import io.ourfit.api.infra.aws.s3.OurfitS3Client;
import io.ourfit.api.infra.persistence.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserCommandServiceImpl extends AbstractEntityFinder<User, Long>
    implements UserCommandService {

  private final UserRepository userRepository;
  private final OAuth2Service oAuth2Service;
  private final WorkoutService workoutService;
  private final OurfitS3Client s3Client;

  public UserCommandServiceImpl(
      UserRepository repository,
      OAuth2Service oAuth2Service,
      WorkoutService workoutService,
      OurfitS3Client s3Client) {
    super(repository);
    this.userRepository = repository;
    this.oAuth2Service = oAuth2Service;
    this.workoutService = workoutService;
    this.s3Client = s3Client;
  }

  @Override
  public User save(UserSignUpDto signUpDto) {
    if (this.userRepository.existsByoAuthId(signUpDto.oAuthId())
        || this.userRepository.existsByNickName(signUpDto.nickname())) {
      throw new DuplicatedException();
    }

    OAuth2UserInfo oAuth2UserInfo = this.oAuth2Service.getUserInfo(signUpDto.oAuthId());
    final User user = this.repository.save(User.of(oAuth2UserInfo, signUpDto));
    // 사용자 등록 후, 선호 운동 종목 등록
    user.setFavoriteWorkouts(this.buildFavoriteWorkouts(user, signUpDto.favoriteWorkouts()));
    return user;
  }

  @Override
  public void updateBasicInfo(final long id, UserBasicInfoUpdateDto updateDto) {
    if (updateDto.nickname() != null
        && this.userRepository.existsByNickName(updateDto.nickname())) {
      throw new DuplicatedException();
    }
    this.ifFoundThen(id, user -> user.updateBasicInfo(updateDto));
  }

  @Override
  public void updateWorkoutPreferences(final long id, UserWorkoutPreferencesUpdateDto updateDto) {
    this.ifFoundThen(
        id,
        user -> {
          user.updateWorkoutPreferences(updateDto);
          if (!updateDto.favoriteWorkouts().isEmpty()) {
            user.setFavoriteWorkouts(
                this.buildFavoriteWorkouts(user, updateDto.favoriteWorkouts()));
          }
        });
  }

  @Override
  public void updateProfile(final long id, UserProfileUpdateDto updateDto) {
    this.ifFoundThen(id, user -> user.setProfile(updateDto));
  }

  @Override
  public void updateProfileImage(final long id, MultipartFile profileImage) {
    this.ifFoundThen(
        id,
        user -> {
          final String profileImageUrl = this.s3Client.upload("버킷/이미지/경로", profileImage);
          user.setProfileImageUrl(profileImageUrl);
        });
  }

  @Override
  public void delete(final long id) {
    this.ifFoundThen(
        id,
        user -> {
          this.oAuth2Service.withdrawal(user.getOAuthId());
          user.delete();
        });
  }

  private Set<UserFavoriteWorkout> buildFavoriteWorkouts(User user, Set<String> favoriteWorkouts) {
    return this.workoutService.findAllByCodeIn(favoriteWorkouts).stream()
        .map(workout -> UserFavoriteWorkout.of(user, workout))
        .collect(Collectors.toUnmodifiableSet());
  }
}
