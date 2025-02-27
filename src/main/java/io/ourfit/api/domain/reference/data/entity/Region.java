package io.ourfit.api.domain.reference.data.entity;

import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "region",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uq_region",
          columnNames = {"region1", "region2", "region3"})
    })
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region extends BaseEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "region1", nullable = false, length = 50)
  private String region1;

  @Column(name = "region2", nullable = false, length = 50)
  private String region2;

  @Column(name = "region3", nullable = false, length = 50)
  private String region3;

  public String getFullName() {
    return String.join(" ", this.region1, this.region2, this.region3);
  }
}
