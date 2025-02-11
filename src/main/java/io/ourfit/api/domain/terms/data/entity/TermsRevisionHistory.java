package io.ourfit.api.domain.terms.data.entity;

import io.ourfit.api.global.data.entity.AuditableBaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "terms_revision_history")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TermsRevisionHistory extends AuditableBaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "terms_id", nullable = false)
  private Terms terms;

  @Enumerated(EnumType.STRING)
  @Column(name = "terms_type", nullable = false)
  private TermsType type;

  @Column(name = "title", nullable = false, length = 150)
  private String title;

  @Lob
  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "version", nullable = false)
  private BigDecimal version;

  @Column(name = "revision_note", nullable = false, columnDefinition = "TEXT")
  private String revisionNote;

  public static TermsRevisionHistory of(Terms terms, String revisionNote) {
    return TermsRevisionHistory.builder()
        .terms(terms)
        .type(terms.getType())
        .title(terms.getTitle())
        .content(terms.getContent())
        .version(terms.getVersion())
        .revisionNote(revisionNote)
        .build();
  }
}
