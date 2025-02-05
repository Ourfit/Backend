package io.ourfit.api.domain.challenge;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.converter.DayOfWeekSetConverter;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "challenge")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mate_id", nullable = false)
  private Mate mate;

  @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "goal_workout_count", columnDefinition = "tinyint unsigned not null")
  private Short goalWorkoutCount;

  @Convert(converter = DayOfWeekSetConverter.class)
  @Column(name = "goal_workout_day_of_week", nullable = false)
  private Set<DayOfWeek> goalWorkoutDayOfWeek;

  @Column(name = "challenge_duration_in_moths", columnDefinition = "tinyint unsigned not null")
  private Short challengeDurationInMoths;

  @NotNull @Column(name = "start_at", nullable = false)
  private Instant startAt;

  @NotNull @Column(name = "end_at", nullable = false)
  private Instant endAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder.Default
  @OneToMany(mappedBy = "challenge")
  private List<ChallengeRecord> challengeRecords = new ArrayList<>();
}
