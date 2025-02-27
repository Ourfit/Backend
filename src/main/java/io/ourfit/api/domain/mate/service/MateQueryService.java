package io.ourfit.api.domain.mate.service;

import io.ourfit.api.domain.mate.data.dto.internal.MateInfoDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;

public interface MateQueryService {

  Optional<Mate> findByIdAndStatus(long mateId, MateStatusType status);

  Optional<MateInfoDto> findCurrentMateInfo(User user);
}
