package io.ourfit.api.global.data.entity;

import io.ourfit.api.global.security.data.OurfitAuditorAware;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * Entity의 생성자와 수정자를 자동으로 관리하기 위한 추상 클래스 <br>
 * {@link OurfitAuditorAware}를 통해 SecurityContext에서 현재 사용자 정보를 가져온다. <br>
 * 모든 Entity 클래스는 이 클래스 또는 {@link BaseEntity}를 상속받도록 구성해야 함!
 */
@Getter
@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AuditableBaseEntity extends BaseEntity {

  @CreatedBy
  @Column(nullable = false, updatable = false)
  private Long createdBy;

  @LastModifiedBy
  @Column(nullable = false)
  private Long updatedBy;
}
