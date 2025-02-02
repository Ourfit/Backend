package io.ourfit.api.domain.user;

import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;

@Entity
@Table(name = "user_favorite_workout_place")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoriteWorkoutPlace extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "int UNSIGNED not null")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "place_name", nullable = false, length = 100)
  private String placeName;

  @Column(name = "address", nullable = false)
  private String address;
}
