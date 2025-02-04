package io.ourfit.api.domain.user.data.entity.association;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoriteWorkoutId implements Serializable {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "workout_id", nullable = false)
  private Long workoutId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    UserFavoriteWorkoutId that = (UserFavoriteWorkoutId) o;
    return Objects.equals(this.workoutId, that.workoutId)
        && Objects.equals(this.userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.workoutId, this.userId);
  }
}
