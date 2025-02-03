package io.ourfit.api.domain.user.entity;

import io.ourfit.api.domain.auth.data.dto.OAuth2UserInfo;
import io.ourfit.api.domain.user.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkoutPlace;
import io.ourfit.api.domain.user.entity.enums.GenderType;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.entity.enums.RoleType;
import io.ourfit.api.domain.user.entity.enums.SkillLevelType;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import io.ourfit.api.global.data.entity.SecuredBaseEntity;
import io.ourfit.api.global.utils.StreamUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(
    name = "user",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_oauth",
          columnNames = {"oauth_id", "oauth_type"}),
      @UniqueConstraint(
          name = "uq_email",
          columnNames = {"email"}),
      @UniqueConstraint(
          name = "uq_nickname",
          columnNames = {"nick_name"})
    },
    indexes = {@Index(name = "idx_region", columnList = "region1, region2, region3")})
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends SecuredBaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  /** 닉네임 변경 가능한 최소 간격 */
  @Transient private static final Duration NICKNAME_UPDATE_INTERVAL = Duration.ofDays(30);

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "oauth_id", nullable = false)
  private String oAuthId;

  @Enumerated(EnumType.STRING)
  @Column(name = "oauth_type", nullable = false)
  private OAuth2ProviderType oAuthProviderType;

  @Enumerated(EnumType.STRING)
  @Column(name = "role_type", nullable = false)
  private RoleType roleType;

  @Email
  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "nick_name", nullable = false, length = 50)
  private String nickName;

  @Column(name = "age", nullable = false, columnDefinition = "tinyint unsigned")
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender_type", nullable = false)
  private GenderType genderType;

  @Lob
  @Column(name = "introduction", columnDefinition = "text")
  private String introduction;

  @Enumerated(EnumType.STRING)
  @Column(name = "skill_level_type", nullable = false)
  private SkillLevelType skillLevelType;

  @Enumerated(EnumType.STRING)
  @Column(name = "preference_type", nullable = false)
  private TimePrefrenceType preferredWorkoutTime;

  @URL(protocol = "https")
  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @URL(protocol = "https", host = "open.kakao.com")
  @Column(name = "open_chat_url")
  private String openChatUrl;

  @Column(name = "region1", nullable = false, length = 50)
  private String region1;

  @Column(name = "region2", nullable = false, length = 50)
  private String region2;

  @Column(name = "region3", nullable = false, length = 50)
  private String region3;

  @Column(name = "nick_name_updated_at")
  private LocalDateTime nickNameUpdatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder.Default
  @OneToMany(mappedBy = "user")
  private List<UserFavoriteWorkout> favoriteWorkouts = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user")
  private List<UserFavoriteWorkoutPlace> favoriteWorkoutPlaces = new ArrayList<>();

  public static User of(OAuth2UserInfo oAuth2UserInfo, UserSignUpDto signUpDto) {
    return User.builder()
        .oAuthId(oAuth2UserInfo.getId())
        .oAuthProviderType(oAuth2UserInfo.getProvider())
        .roleType(RoleType.USER)
        .email(oAuth2UserInfo.getEmail())
        .name(oAuth2UserInfo.getName())
        .nickName(signUpDto.nickname())
        .age(signUpDto.age())
        .genderType(signUpDto.gender())
        .skillLevelType(signUpDto.skillLevel())
        .preferredWorkoutTime(signUpDto.preferredWorkoutTime())
        .region1(signUpDto.region1())
        .region2(signUpDto.region2())
        .region3(signUpDto.region3())
        .build();
  }

  public boolean isSuperAdmin() {
    return this.roleType.isSuperAdmin();
  }

  public boolean isAdmin() {
    return this.roleType.isAdmin();
  }

  public boolean isNicknameUpdatable() {
    return this.nickNameUpdatedAt == null
        || this.nickNameUpdatedAt.plus(NICKNAME_UPDATE_INTERVAL).isBefore(LocalDateTime.now());
  }

  public void updateBasicInfo(UserBasicInfoUpdateDto updateDto) {
    if (updateDto.nickname() != null) {
      if (!this.isNicknameUpdatable()) {
        throw new IllegalStateException("Nickname can only be updated once every 30 days");
      }
      this.nickName = updateDto.nickname();
      this.nickNameUpdatedAt = LocalDateTime.now();
    }
    if (updateDto.age() != null) {
      this.age = updateDto.age();
    }
    if (Stream.of(updateDto.region1(), updateDto.region2(), updateDto.region3())
        .allMatch(Objects::nonNull)) {
      this.region1 = updateDto.region1();
      this.region2 = updateDto.region2();
      this.region3 = updateDto.region3();
    }
    if (updateDto.skillLevel() != null) {
      this.skillLevelType = updateDto.skillLevel();
    }
  }

  public void updateWorkoutPreferences(UserWorkoutPreferencesUpdateDto updateDto) {
    if (updateDto.preferredWorkoutTime() != null) {
      this.preferredWorkoutTime = updateDto.preferredWorkoutTime();
    }
    if (!updateDto.favoritePlaces().isEmpty()) {
      // TODO: clear하고 새로 넣기 OR 변경된 것만 업데이트?
      this.favoriteWorkoutPlaces =
          StreamUtils.convert(
              updateDto.favoritePlaces(), place -> UserFavoriteWorkoutPlace.of(this, place));
    }
  }

  public void updateFavoriteWorkouts(List<UserFavoriteWorkout> favoriteWorkouts) {
    this.favoriteWorkouts = favoriteWorkouts;
  }

  public void updateProfileImage(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  public void updateOpenChatUrl(UserProfileUpdateDto upsertDto) {
    if (upsertDto.introduction() != null) {
      this.introduction = upsertDto.introduction();
    }
    if (upsertDto.openChatUrl() != null) {
      this.openChatUrl = upsertDto.openChatUrl();
    }
  }
}
