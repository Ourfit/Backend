package io.ourfit.api.domain.mate.data.entity;

import io.ourfit.api.domain.mate.data.enums.MateActionType;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "mate_history")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MateHistory extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mate_id", nullable = false)
  private Mate mate;

  @Enumerated(EnumType.STRING)
  @Column(name = "action_type", nullable = false, updatable = false)
  private MateActionType actionType;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "actor_id", nullable = false)
  private User actor;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "target_id", nullable = false)
  private User target;

  @ColumnDefault("0")
  @Column(name = "actor_read")
  private boolean actorRead;

  @ColumnDefault("0")
  @Column(name = "target_read")
  private boolean targetRead;

  public static MateHistory from(MateActionType mateActionType, Mate mate) {
    return MateHistory.builder()
        .mate(mate)
        .actionType(mateActionType)
        .actor(mate.getMe())
        .target(mate.getMyMate())
        .build();
  }

  public void markAsRead(long userId) {
    if (this.actor.getId().equals(userId)) {
      this.actorRead = true;
    } else if (this.target.getId().equals(userId)) {
      this.targetRead = true;
    } else {
      throw new IllegalArgumentException("이 이력에 대한 권한이 없음");
    }
  }
}
