package io.ourfit.api.domain.challenge.data.entity;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeRecordAsDoneDto;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "challenge_record")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeRecord extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "challenge_id", nullable = false)
  private Challenge challenge;

  @Setter
  @Column(name = "record_date", nullable = false)
  private LocalDate recordDate;

  @ColumnDefault("0")
  @Column(name = "is_completed")
  private Boolean isCompleted;

  @Column(name = "intensity_level", columnDefinition = "tinyint unsigned")
  private Short intensityLevel;

  @Lob
  @Column(name = "note", columnDefinition = "text")
  private String note;

  public static ChallengeRecord ofNew(Challenge challenge, LocalDate recordDate) {
    return ChallengeRecord.builder()
        .challenge(challenge)
        .recordDate(recordDate)
        .isCompleted(false)
        .intensityLevel(null)
        .note(null)
        .build();
  }

  public void markAsDone(ChallengeRecordAsDoneDto recordDto) {
    this.isCompleted = true;
    this.intensityLevel = recordDto.intensityLevel();
  }

  public boolean isChallengeDay() {
    return this.recordDate.equals(LocalDate.now(CLOCK));
  }

  public boolean isDone() {
    return this.isCompleted;
  }

  public boolean isNotDone() {
    return !this.isCompleted;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
      return false;
    }
    ChallengeRecord that = (ChallengeRecord) obj;
    return Objects.equals(this.challenge, that.challenge)
        && Objects.equals(this.recordDate, that.recordDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.challenge, this.recordDate);
  }
}
