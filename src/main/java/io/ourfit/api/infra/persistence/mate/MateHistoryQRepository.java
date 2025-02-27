package io.ourfit.api.infra.persistence.mate;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateHistorySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MateHistoryQRepository {

  Page<MateHistoryDto> findAllByUserId(
      long userId, MateHistorySearchDto searchDto, Pageable pageable);
}
