package io.ourfit.api.infra.persistence.challenge;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

  @Query("SELECT c FROM Challenge c WHERE c.mate = :mate AND c.deletedAt IS NULL")
  @EntityGraph(attributePaths = {"challengeRecords"})
  List<Challenge> findAllByMate(Mate mate);

  boolean existsByMateAndUserAndDeletedAtIsNull(Mate mate, User user);
}
