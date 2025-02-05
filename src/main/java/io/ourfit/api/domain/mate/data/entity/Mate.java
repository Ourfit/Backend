package io.ourfit.api.domain.mate.data.entity;

import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.enums.MateStatusType;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "mate")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mate extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "requester_id", nullable = false)
  private User requester;

  @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "requestee_id", nullable = false)
  private User requestee;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private MateStatusType statusType;

  @Column(name = "matched_at")
  private LocalDateTime matchedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public static Mate of(User requester, User requestee) {
    return Mate.builder()
        .requester(requester)
        .requestee(requestee)
        .statusType(MateStatusType.PENDING)
        .build();
  }

  public void accept() {
    this.statusType = MateStatusType.MATCHED;
    this.matchedAt = LocalDateTime.now();
  }

  public void delete() {
    this.statusType = MateStatusType.DELETED;
    this.deletedAt = LocalDateTime.now();
  }
}
