package io.ourfit.api.domain.mate.data.entity;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mate extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "me_id", nullable = false)
  private User me;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "my_mate_id", nullable = false)
  private User myMate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private MateStatusType statusType;

  @Column(name = "accepted_at")
  private LocalDateTime acceptedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder.Default
  @OneToMany(
      mappedBy = "mate",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.MERGE},
      orphanRemoval = true)
  private Set<Challenge> challenges = new HashSet<>();

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
}
