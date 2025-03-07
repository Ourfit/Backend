package io.ourfit.api.domain.user.data.entity.association;

import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.data.entity.Workout;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(name = "user_favorite_workout")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoriteWorkout extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @EmbeddedId private UserFavoriteWorkoutId id;

  @MapsId("userId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @MapsId("workoutId")
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "workout_id", nullable = false)
  private Workout workout;

  public static UserFavoriteWorkout of(User user, Workout workout) {
    UserFavoriteWorkoutId id = new UserFavoriteWorkoutId(user.getId(), workout.getId());
    return new UserFavoriteWorkout(id, user, workout);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
      return false;
    }
    UserFavoriteWorkout that = (UserFavoriteWorkout) obj;
    return Objects.equals(this.user.getId(), that.user.getId())
        && Objects.equals(this.workout.getCode(), that.workout.getCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.user.getId(), this.workout.getCode());
  }
}
