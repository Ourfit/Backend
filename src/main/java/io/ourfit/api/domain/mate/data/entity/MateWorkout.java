package io.ourfit.api.domain.mate.data.entity;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutPlacesUpsertDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutTimeUpsertDto;
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

  @Column(name = "place_name", length = 100)
  private String placeName;

  @Column(name = "address")
  private String address;

  @Convert(converter = DayOfWeekSetConverter.class)
  @Column(name = "workout_day_of_week")
  private Set<DayOfWeek> workoutDayOfWeek;

  @Column(name = "workout_start_at")
  private LocalTime workoutStartAt;

  @Column(name = "workout_end_at")
  private LocalTime workoutEndAt;

  public static MateWorkout ofEmpty(Mate mate) {
    return MateWorkout.builder().mate(mate).build();
  }

  public void putWorkoutPlaces(MateWorkoutPlacesUpsertDto upsertDto) {
    this.placeName = upsertDto.placeName();
    this.address = upsertDto.address();
  }

  public void putWorkoutTimes(MateWorkoutTimeUpsertDto upsertDto) {
    this.workoutDayOfWeek = upsertDto.workoutDays();
    this.workoutStartAt = upsertDto.startAt();
    this.workoutEndAt = upsertDto.endAt();
  }
}
