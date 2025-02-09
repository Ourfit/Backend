package io.ourfit.api.domain.mate.service;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateHistoryService {

  Page<MateHistoryDto> findAllByUserId(long userId, Pageable pageable);

  void markAsRead(long userId, long historyId);
}
