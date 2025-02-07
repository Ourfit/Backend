package io.ourfit.api.domain.challenge.data.entity;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.entity.BaseEntity;
import io.ourfit.api.global.persistence.converter.DayOfWeekSetConverter;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mate_id", nullable = false)
  private Mate mate;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "goal_workout_count", columnDefinition = "tinyint unsigned not null")
  private Short goalWorkoutCount;

  @Setter
  @Convert(converter = DayOfWeekSetConverter.class)
  @Column(name = "goal_workout_day_of_week", nullable = false)
  private Set<DayOfWeek> goalWorkoutDayOfWeek;

  @Column(name = "challenge_duration_in_moths", columnDefinition = "tinyint unsigned not null")
  private Short challengeDurationInMoths;

  @Column(name = "start_at", nullable = false)
  private LocalDate startAt;

  @Column(name = "end_at", nullable = false)
  private LocalDate endAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder.Default
  @OneToMany(mappedBy = "challenge")
  private List<ChallengeRecord> challengeRecords = new ArrayList<>();

  public static Challenge of(Mate mate, User challenger, NewChallengeDto challengeDto) {
    return Challenge.builder()
        .mate(mate)
        .user(challenger)
        .goalWorkoutCount(challengeDto.goalWorkoutCount())
        .goalWorkoutDayOfWeek(challengeDto.goalWorkoutDayOfWeek())
        .challengeDurationInMoths(challengeDto.challengeDurationInMoths())
        .startAt(challengeDto.startAt())
        .endAt(challengeDto.endAt())
        .build();
  }

  public boolean isOwner(User user) {
    return this.user.getId().equals(user.getId());
  }

  public boolean isWorkoutDay() {
    LocalDate today = LocalDate.now();
    return this.goalWorkoutDayOfWeek.contains(today.getDayOfWeek())
        && (today.isEqual(this.startAt) || today.isAfter(this.startAt))
        && (today.isEqual(this.endAt) || today.isBefore(this.endAt));
  }
}
