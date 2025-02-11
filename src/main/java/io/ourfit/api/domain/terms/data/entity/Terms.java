package io.ourfit.api.domain.terms.data.entity;

import io.ourfit.api.domain.terms.data.dto.internal.TermsUpsertDto;
import io.ourfit.api.global.data.entity.AuditableBaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@Table(
    name = "terms",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_type_version",
          columnNames = {"type", "version"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Terms extends AuditableBaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Transient private static final BigDecimal INITIAL_VERSION = BigDecimal.valueOf(1.0);
  @Transient private static final BigDecimal VERSION_INCREMENT = BigDecimal.valueOf(0.1);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "terms_type", nullable = false)
  private TermsType type;

  @Column(name = "is_required", nullable = false)
  private boolean isRequired;

  @Column(name = "title", nullable = false, length = 150)
  private String title;

  @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "version", nullable = false)
  private BigDecimal version;

  @Builder.Default
  @OneToMany(mappedBy = "terms", fetch = FetchType.LAZY)
  private Set<TermsRevisionHistory> revisionHistories = new HashSet<>();

  public static Terms create(TermsUpsertDto upsertDto) {
    return Terms.builder()
        .type(upsertDto.type())
        .isRequired(upsertDto.isRequired())
        .title(upsertDto.title())
        .content(upsertDto.content())
        .version(INITIAL_VERSION)
        .build();
  }

  public Terms update(TermsUpsertDto upsertDto) {
    return this.toBuilder()
        .isRequired(upsertDto.isRequired())
        .title(upsertDto.title())
        .content(upsertDto.content())
        .version(this.version.add(VERSION_INCREMENT))
        .build();
  }
}
