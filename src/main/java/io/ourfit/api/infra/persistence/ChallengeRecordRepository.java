package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {

  Optional<ChallengeRecord> findByChallengeAndRecordDate(Challenge challenge, LocalDate recordDate);

  List<ChallengeRecord> findAllByChallengeAndRecordDateBetween(
      Challenge challenge, LocalDate start, LocalDate end);
}
