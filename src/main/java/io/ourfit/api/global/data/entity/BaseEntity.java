package io.ourfit.api.global.data.entity;

import static io.ourfit.api.global.config.TimeConfig.DEFAULT_ZONE_ID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ourfit.api.global.data.RedisSerializable;
import io.ourfit.api.global.data.Versionable;
import io.ourfit.api.global.utils.DateTimeFormatUtils;
import io.ourfit.api.global.utils.HashUtils;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity의 생성일시와 수정일시를 자동으로 관리하기 위한 추상 클래스 <br>
 * 모든 Entity 클래스는 이 클래스 또는 {@link AuditableBaseEntity}를 상속받도록 구성해야 함!
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEntity implements Serializable, RedisSerializable, Versionable {

  @Transient protected static final Clock CLOCK = Clock.system(DEFAULT_ZONE_ID);

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Override
  @JsonIgnore
  public String getETag() {
    return HashUtils.hash("MD5", this.createdAt.toString(), this.updatedAt.toString());
  }

  @Override
  @JsonIgnore
  public String getLastModified() {
    return DateTimeFormatUtils.toRFC1123String(this.updatedAt);
  }
}
