package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.dto.internal.MateInfoDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.service.MateQueryService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.data.enums.MateStatusType;
import io.ourfit.api.infra.persistence.MateQRepository;
import io.ourfit.api.infra.persistence.MateRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MateQueryServiceImpl implements MateQueryService {

  private final MateRepository repository;
  private final MateQRepository qRepository;

  @Override
  public Optional<Mate> findByIdAndStatus(final long mateId, MateStatusType status) {
    return this.repository.findByIdAndStatusType(mateId, status);
  }

  @Override
  public Optional<MateInfoDto> findCurrentMateInfo(User user) {
    return this.qRepository.findCurrentMateInfo(user);
  }
}
