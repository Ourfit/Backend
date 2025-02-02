package io.ourfit.api.domain.workout;

import io.ourfit.api.domain.user.entity.User;
import io.ourfit.api.domain.workout.enums.MateStatusType;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
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
}
