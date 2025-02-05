package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.mate.data.entity.MateLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateLogRepository extends JpaRepository<MateLog, Long> {}
