package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.mate.data.entity.MateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateHistoryRepository extends JpaRepository<MateHistory, Long> {}
