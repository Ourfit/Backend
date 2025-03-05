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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

  @Setter
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
  private List<ChallengeRecord> challengeRecords = new ArrayList<>();

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

  public void updatePlannedRecords(Set<DayOfWeek> newGoalDayOfWeeks) {
    if (this.goalWorkoutDayOfWeeks.equals(newGoalDayOfWeeks)) {
      return; // 목표 요일이 변경되지 않았다면 업데이트 필요 없음
    }
    // 미래 기록만 필터링
    LocalDate today = LocalDate.now();
    Iterator<ChallengeRecord> futureRecordsIterator =
        this.challengeRecords.stream()
            .filter(challengeRecord -> challengeRecord.getRecordDate().isAfter(today))
            .iterator();
    LocalDate currentDate = today.plusDays(1);

    while (!currentDate.isAfter(this.endAt) && futureRecordsIterator.hasNext()) {
      var challengeRecord = futureRecordsIterator.next();
      if (newGoalDayOfWeeks.contains(currentDate.getDayOfWeek())) {
        challengeRecord.setRecordDate(currentDate);
      } else {
        futureRecordsIterator.remove(); // 새로운 요일에 맞지 않으면 삭제
      }
      currentDate = currentDate.plusDays(1);
    }

    // Records 수가 부족하면 새로운 기록 추가
    this.challengeRecords.addAll(this.generateRecords(newGoalDayOfWeeks, currentDate, this.endAt));
  }

  public boolean isOwner(User user) {
    return this.user.getId().equals(user.getId());
  }

  public long calculateCompletionRate() {
    long totalDays = Math.max(1, ChronoUnit.DAYS.between(this.startAt, this.endAt) + 1);
    long doneDays = this.challengeRecords.stream().filter(ChallengeRecord::isDone).count();
    return (doneDays * 100) / totalDays;
  }

  public long calculateDayElapsed() {
    if (this.startAt.isAfter(LocalDate.now())) {
      return 0;
    }
    return Math.max(1, ChronoUnit.DAYS.between(this.startAt, LocalDate.now()) + 1);
  }

  public long calculateRemainingDays() {
    return ChronoUnit.DAYS.between(LocalDate.now(), this.endAt);
  }

  private List<ChallengeRecord> generateRecords(
      Set<DayOfWeek> goalDays, LocalDate start, LocalDate end) {
    return Stream.iterate(start, date -> date.plusDays(1))
        .limit(ChronoUnit.DAYS.between(start, end) + 1)
        .filter(date -> goalDays.contains(date.getDayOfWeek()))
        .map(date -> ChallengeRecord.ofNew(this, date))
        .toList();
  }

  public void delete() {
    this.deletedAt = LocalDateTime.now();
  }
}
