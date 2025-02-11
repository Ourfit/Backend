package io.ourfit.api.infra.persistence.user;

import io.ourfit.api.domain.user.data.entity.association.UserTermsAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermsAgreementRepository extends JpaRepository<UserTermsAgreement, Long> {}
