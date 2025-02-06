package io.ourfit.api.domain.mate.data.entity;

import io.ourfit.api.domain.mate.service.impl.MateEntityListener;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.enums.MateStatusType;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.*;

@Entity
@Table(
    name = "mate",
    indexes = {
      @Index(name = "idx_mate_requester", columnList = "status_type, requester_id"),
      @Index(name = "idx_mate_requestee", columnList = "status_type, requestee_id"),
    })
@Getter
@Builder
@EntityListeners(MateEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mate extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "me_id", nullable = false)
  private User me;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "my_mate_id", nullable = false)
  private User myMate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private MateStatusType statusType;

  @Column(name = "accepted_at")
  private LocalDateTime acceptedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  public static Mate of(User me, User myMate) {
    return Mate.builder().me(me).myMate(myMate).statusType(MateStatusType.PENDING).build();
  }

  public void accept() {
    this.statusType = MateStatusType.MATCHED;
    this.acceptedAt = LocalDateTime.now();
  }

  public boolean canAccept(final long userId) {
    return this.statusType == MateStatusType.PENDING && this.myMate.getId().equals(userId);
  }

  public void unmate() {
    this.statusType = MateStatusType.UNMATED;
    this.deletedAt = LocalDateTime.now();
  }

  public boolean canUnmate(final long userId) {
    return this.statusType == MateStatusType.MATCHED
        && (this.me.getId().equals(userId) || this.myMate.getId().equals(userId));
  }

  public long getDaysSinceAccepted() {
    if (this.acceptedAt == null) {
      return -1;
    }
    if (this.statusType == MateStatusType.UNMATED) {
      return ChronoUnit.DAYS.between(this.acceptedAt, this.deletedAt);
    }
    return ChronoUnit.DAYS.between(this.acceptedAt, LocalDateTime.now());
  }
}
