package io.ourfit.api.domain.workout;

import io.ourfit.api.global.converter.DayOfWeekSetConverter;
import io.ourfit.api.global.data.entity.SecuredBaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "mate_workout")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MateWorkout extends SecuredBaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @Column(name = "mate_id")
  private Long id;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mate_id", nullable = false)
  private Mate mate;

  @Column(name = "place_name", nullable = false, length = 100)
  private String placeName;

  @Column(name = "address", nullable = false)
  private String address;

  @Convert(converter = DayOfWeekSetConverter.class)
  @Column(name = "workout_day_of_week", nullable = false)
  private Set<DayOfWeek> workoutDayOfWeek;

  @Column(name = "workout_start_at", nullable = false)
  private LocalTime workoutStartAt;

  @Column(name = "workout_end_at", nullable = false)
  private LocalTime workoutEndAt;
}
