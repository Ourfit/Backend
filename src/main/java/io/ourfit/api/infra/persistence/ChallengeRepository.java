package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.workout.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {}
