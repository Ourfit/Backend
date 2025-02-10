package io.ourfit.api.domain.user.data.entity.association;

import io.ourfit.api.domain.user.data.dto.internal.UserFavoritePlacesUpsertDto;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "user_favorite_workout_place")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoriteWorkoutPlace extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "place_name", nullable = false, length = 100)
  private String placeName;

  @Column(name = "address", nullable = false)
  private String address;

  public static UserFavoriteWorkoutPlace of(User user, UserFavoritePlacesUpsertDto upsertDto) {
    return UserFavoriteWorkoutPlace.builder()
        .user(user)
        .placeName(upsertDto.placeName())
        .address(upsertDto.address())
        .build();
  }

  public static Set<UserFavoriteWorkoutPlace> of(
      User user, List<UserFavoritePlacesUpsertDto> favoritePlaces) {
    return favoritePlaces.stream()
        .map(place -> UserFavoriteWorkoutPlace.of(user, place))
        .collect(Collectors.toUnmodifiableSet());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
      return false;
    }
    UserFavoriteWorkoutPlace that = (UserFavoriteWorkoutPlace) obj;
    return Objects.equals(this.placeName, that.placeName)
        && Objects.equals(this.address, that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.placeName, this.address);
  }
}
