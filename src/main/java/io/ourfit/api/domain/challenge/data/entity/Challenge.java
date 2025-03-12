package io.ourfit.api.domain.challenge.data.entity;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeCreateDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.entity.BaseEntity;
import io.ourfit.api.global.persistence.converter.DayOfWeekSetConverter;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;

@Entity
@Table(
    name = "challenge",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_challenge_mate_user",
          columnNames = {"user_id", "mate_id", "deleted_at"})
    })
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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(
      name = "goal_workout_count",
      nullable = false,
      updatable = false,
      columnDefinition = "tinyint unsigned")
  private Short goalWorkoutCount;

  @Convert(converter = DayOfWeekSetConverter.class)
  @Column(name = "goal_workout_day_of_week", nullable = false)
  private Set<DayOfWeek> goalWorkoutDayOfWeeks;

  @Column(
      name = "challenge_duration_in_months",
      nullable = false,
      updatable = false,
      columnDefinition = "tinyint unsigned")
  private Short challengeDurationInMonths;

  @Column(name = "start_at", nullable = false, updatable = false)
  private LocalDate startAt;

  @Column(name = "end_at", nullable = false, updatable = false)
  private LocalDate endAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder.Default
  @OneToMany(
      mappedBy = "challenge",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private Set<ChallengeRecord> challengeRecords = new HashSet<>();

  public static Challenge of(Mate mate, User challenger, ChallengeCreateDto challengeDto) {
    return Challenge.builder()
        .mate(mate)
        .user(challenger)
        .goalWorkoutCount(challengeDto.goalWorkoutCount())
        .goalWorkoutDayOfWeeks(challengeDto.goalWorkoutDayOfWeeks())
        .challengeDurationInMonths(challengeDto.challengeDurationInMonths())
        .startAt(challengeDto.startAt())
        .endAt(challengeDto.endAt())
        .build();
  }

  public void createChallengeRecords() {
    this.challengeRecords =
        this.generateRecords(this.goalWorkoutDayOfWeeks, this.startAt, this.endAt);
  }

  public void setNewGoalDayOfWeeks(Set<DayOfWeek> newGoalDayOfWeeks) {
    if (this.goalWorkoutDayOfWeeks.containsAll(newGoalDayOfWeeks)) {
      return; // 목표 요일이 변경되지 않았다면 업데이트 X
    }
    LocalDate now = LocalDate.now();
    final boolean isChallengeStarted = this.startAt.isEqual(now) || this.startAt.isBefore(now);
    LocalDate newStartDate =
        isChallengeStarted ? now.plusWeeks(1).with(DayOfWeek.MONDAY) : this.startAt;

    List<ChallengeRecord> removableRecords =
        this.getRemovableRecords(newGoalDayOfWeeks, newStartDate);
    removableRecords.forEach(this.challengeRecords::remove);

    this.challengeRecords.addAll(this.generateRecords(newGoalDayOfWeeks, newStartDate, this.endAt));
    this.goalWorkoutDayOfWeeks = newGoalDayOfWeeks;
  }

  public boolean isOwner(User user) {
    return this.user.getId().equals(user.getId());
  }

  public long calculateCompletionRate() {
    long totalRecords = Math.max(1, this.challengeRecords.size());
    long doneRecords = this.challengeRecords.stream().filter(ChallengeRecord::isDone).count();
    return (doneRecords * 100) / totalRecords;
  }

  public long calculateDayElapsed() {
    LocalDate now = LocalDate.now();
    if (this.startAt.isAfter(now)) {
      return 0;
    }
    return Math.max(1, ChronoUnit.DAYS.between(this.startAt, now) + 1);
  }

  public long calculateRemainingDays() {
    return ChronoUnit.DAYS.between(LocalDate.now(), this.endAt);
  }

  public void delete() {
    this.deletedAt = LocalDateTime.now();
  }

  private Set<ChallengeRecord> generateRecords(
      Set<DayOfWeek> goalDays, LocalDate start, LocalDate end) {
    return Stream.iterate(start, date -> date.plusDays(1))
        .limit(ChronoUnit.DAYS.between(start, end) + 1)
        .filter(date -> goalDays.contains(date.getDayOfWeek()))
        .map(date -> ChallengeRecord.ofNew(this, date))
        .collect(Collectors.toSet());
  }

  private List<ChallengeRecord> getRemovableRecords(
      Set<DayOfWeek> newGoalDayOfWeeks, LocalDate newStartDate) {
    return this.challengeRecords.stream()
        .filter(ChallengeRecord::isNotDone)
        .filter(cRecord -> cRecord.getRecordDate().isAfter(newStartDate))
        .filter(cRecord -> !newGoalDayOfWeeks.contains(cRecord.getRecordDate().getDayOfWeek()))
        .toList();
  }
}
