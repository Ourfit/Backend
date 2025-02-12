package io.ourfit.api.domain.user.data.entity;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.user.data.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.data.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkoutPlace;
import io.ourfit.api.domain.user.data.entity.enums.GenderType;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.data.entity.enums.RoleType;
import io.ourfit.api.domain.user.data.entity.enums.SkillLevelType;
import io.ourfit.api.domain.workout.data.enums.TimePrefrenceType;
import io.ourfit.api.global.data.entity.AuditableBaseEntity;
import io.ourfit.api.global.exception.custom.IllegalEntityStateException;
import io.ourfit.api.global.persistence.converter.EncryptedStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(
    name = "user",
    indexes = {@Index(name = "idx_region", columnList = "region1, region2, region3")})
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditableBaseEntity {

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
  @Convert(converter = EncryptedStringConverter.class)
  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "nick_name", nullable = false, length = 50)
  private String nickname;

  @Column(name = "age", nullable = false, columnDefinition = "tinyint unsigned")
  private Integer age;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender_type", nullable = false, updatable = false)
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

  @Setter
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
  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private Set<UserFavoriteWorkout> favoriteWorkouts = new HashSet<>();

  @Builder.Default
  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private Set<UserFavoriteWorkoutPlace> favoriteWorkoutPlaces = new HashSet<>();

  public static User of(OAuth2UserInfo oAuth2UserInfo, UserSignUpDto signUpDto) {
    return User.builder()
        .oAuthId(oAuth2UserInfo.getId())
        .oAuthProviderType(oAuth2UserInfo.getProvider())
        .roleType(RoleType.USER)
        .email(oAuth2UserInfo.getEmail())
        .nickname(signUpDto.nickname())
        .age(signUpDto.age())
        .genderType(signUpDto.gender())
        .skillLevelType(signUpDto.skillLevel())
        .preferredWorkoutTime(signUpDto.preferredWorkoutTime())
        .region1(signUpDto.region1())
        .region2(signUpDto.region2())
        .region3(signUpDto.region3())
        .build();
  }

  public boolean isEnabled() {
    return this.deletedAt == null;
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

  public void updateBasicInfo(UserBasicInfoUpdateDto basicInfoDto) {
    if (basicInfoDto.nickname() != null) {
      if (!this.isNicknameUpdatable()) {
        throw new IllegalEntityStateException();
      }
      this.nickname = basicInfoDto.nickname();
      this.nickNameUpdatedAt = LocalDateTime.now();
    }
    if (basicInfoDto.age() != null) {
      this.age = basicInfoDto.age();
    }
    if (Stream.of(basicInfoDto.region1(), basicInfoDto.region2(), basicInfoDto.region3())
        .allMatch(Objects::nonNull)) {
      this.region1 = basicInfoDto.region1();
      this.region2 = basicInfoDto.region2();
      this.region3 = basicInfoDto.region3();
    }
    if (basicInfoDto.skillLevel() != null) {
      this.skillLevelType = basicInfoDto.skillLevel();
    }
  }

  public void setPreferredWorkoutTime(TimePrefrenceType newPreferredTime) {
    if (newPreferredTime != null) {
      this.preferredWorkoutTime = newPreferredTime;
    }
  }

  public void setProfile(UserProfileUpdateDto profileDto) {
    this.introduction = profileDto.introduction();
    this.openChatUrl = profileDto.openChatUrl();
  }

  public void setFavoriteWorkoutPlaces(Set<UserFavoriteWorkoutPlace> newFavoritesPlaces) {
    this.favoriteWorkoutPlaces.removeIf(item -> !newFavoritesPlaces.contains(item));
    this.favoriteWorkoutPlaces.addAll(newFavoritesPlaces);
  }

  public void setFavoriteWorkouts(Set<UserFavoriteWorkout> newFavoritesWorkouts) {
    this.favoriteWorkouts.removeIf(item -> !newFavoritesWorkouts.contains(item));
    this.favoriteWorkouts.addAll(newFavoritesWorkouts);
  }

  public void delete() {
    this.deletedAt = LocalDateTime.now();
  }
}
