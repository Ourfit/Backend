package io.ourfit.api.domain.workout;

import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.enums.MateLogActionType;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "mate_request_log")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MateRequestLog extends BaseEntity {

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

  @Enumerated(EnumType.STRING)
  @Column(name = "action_type", nullable = false)
  private MateLogActionType actionType;

  @ColumnDefault("0")
  @Column(name = "is_read")
  private Boolean isRead;
}
