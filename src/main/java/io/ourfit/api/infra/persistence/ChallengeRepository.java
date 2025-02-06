package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {}
