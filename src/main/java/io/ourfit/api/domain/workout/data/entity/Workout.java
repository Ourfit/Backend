package io.ourfit.api.domain.workout.data.entity;

import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Entity
@Table(
    name = "workout_type",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_workout_type",
          columnNames = {"code"})
    })
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workout extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, length = 50)
  private String code;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
      return false;
    }
    Workout that = (Workout) obj;
    return this.code.equals(that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.code);
  }
}
