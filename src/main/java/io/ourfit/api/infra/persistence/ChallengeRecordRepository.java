package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.challenge.ChallengeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {}
