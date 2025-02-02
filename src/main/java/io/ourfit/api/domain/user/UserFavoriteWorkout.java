package io.ourfit.api.domain.user;

import io.ourfit.api.domain.workout.Workout;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;

@Entity
@Table(name = "user_favorite_workout")
@Getter
@Builder
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
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "workout_id", nullable = false)
  private Workout workout;
}
