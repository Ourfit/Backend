package io.ourfit.api.domain.user.entity;

import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkoutPlace;
import io.ourfit.api.domain.user.entity.enums.GenderType;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.entity.enums.RoleType;
import io.ourfit.api.domain.user.entity.enums.SkillLevelType;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import io.ourfit.api.global.data.entity.SecuredBaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "oauth_id", nullable = false)
  private String oauthId;

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
  @Column(name = "weekday_preference_type", nullable = false)
  private TimePrefrenceType weekdayPreferenceType;

  @Enumerated(EnumType.STRING)
  @Column(name = "weekend_preference_type", nullable = false)
  private TimePrefrenceType weekendPreferenceType;

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

  @Column(name = "nick_name_changed_at")
  private LocalDateTime nickNameChangedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder.Default
  @OneToMany(mappedBy = "user")
  private List<UserFavoriteWorkout> favoriteWorkouts = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user")
  private List<UserFavoriteWorkoutPlace> favoriteWorkoutPlaces = new ArrayList<>();
}
