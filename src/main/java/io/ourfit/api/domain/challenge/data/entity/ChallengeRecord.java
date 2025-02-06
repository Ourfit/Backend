package io.ourfit.api.domain.challenge.data.entity;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeRecordDto;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.LocalDate;
import lombok.*;
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

  @Column(name = "record_date", nullable = false)
  private LocalDate recordDate;

  @ColumnDefault("0")
  @Column(name = "is_completed")
  private Boolean isCompleted;

  @ColumnDefault("'2'")
  @Column(name = "intensity_level", columnDefinition = "tinyint unsinged")
  private Short intensityLevel;

  @Lob
  @Column(name = "note", columnDefinition = "text")
  private String note;

  public static ChallengeRecord of(
      Challenge challenge, NewChallengeRecordDto newChallengeRecordDto) {
    return ChallengeRecord.builder()
        .challenge(challenge)
        .recordDate(LocalDate.now())
        .isCompleted(true)
        .intensityLevel(newChallengeRecordDto.intensityLevel())
        .note(null)
        .build();
  }
}
