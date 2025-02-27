package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateHistorySearchDto;
import io.ourfit.api.domain.mate.service.MateHistoryService;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.mate.MateHistoryQRepository;
import io.ourfit.api.infra.persistence.mate.MateHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MateHistoryServiceImpl implements MateHistoryService {

  private final MateHistoryRepository repository;
  private final MateHistoryQRepository qRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<MateHistoryDto> findAllByUserId(
      long userId, MateHistorySearchDto searchDto, Pageable pageable) {
    return this.qRepository.findAllByUserId(userId, searchDto, pageable);
  }

  @Override
  public void markAsRead(final long userId, final long historyId) {
    this.repository
        .findById(historyId)
        .ifPresentOrElse(history -> history.markAsRead(userId), NoSuchEntityException::new);
  }
}
