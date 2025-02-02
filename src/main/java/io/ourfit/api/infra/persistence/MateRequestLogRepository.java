package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.workout.MateRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRequestLogRepository extends JpaRepository<MateRequestLog, Long> {}
