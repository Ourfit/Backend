package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateHistoryQRepository {

  Page<MateHistoryDto> findAllByUserId(long userId, Pageable pageable);
}
