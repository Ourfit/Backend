package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.MateLog;
import io.ourfit.api.domain.workout.enums.MateLogActionType;
import io.ourfit.api.global.data.AbstractJpaRepositoryAware;
import io.ourfit.api.infra.persistence.MateLogRepository;
import jakarta.persistence.PostPersist;

public class MateEntityListener extends AbstractJpaRepositoryAware<MateLogRepository> {

  @Override
  protected Class<MateLogRepository> repositoryType() {
    return MateLogRepository.class;
  }

  @PostPersist
  public void postPersist(Mate mate) {
    this.repository.save(MateLog.fromEntity(MateLogActionType.REQUEST, mate));
  }
}
