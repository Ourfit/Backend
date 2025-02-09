package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

  @Query("select c from Challenge c where c.user.id = :userId and c.deletedAt is null")
  @EntityGraph(attributePaths = {"challengeRecords"})
  Optional<Challenge> findByIdWithRecords(long userId);

  boolean existsByMateAndUser(Mate mate, User user);
}
