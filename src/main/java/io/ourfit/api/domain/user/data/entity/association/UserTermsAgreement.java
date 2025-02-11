package io.ourfit.api.domain.user.data.entity.association;

import io.ourfit.api.domain.terms.data.entity.Terms;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.entity.BaseEntity;
import jakarta.persistence.*;
import java.io.Serial;
import lombok.*;

@Entity
@Getter
@Builder
@Table(
    name = "user_terms_agreement",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_user_terms",
          columnNames = {"user_id", "terms_id"})
    })
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsAgreement extends BaseEntity {

  @Serial private static final long serialVersionUID = 2025020101L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "terms_id", nullable = false)
  private Terms terms;

  public static UserTermsAgreement of(User user, Terms agreement) {
    return UserTermsAgreement.builder().user(user).terms(agreement).build();
  }
}
