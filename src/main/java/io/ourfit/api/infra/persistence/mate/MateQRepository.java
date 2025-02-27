package io.ourfit.api.infra.persistence.mate;

import io.ourfit.api.domain.mate.data.dto.internal.MateInfoDto;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;

public interface MateQRepository {

  Optional<MateInfoDto> findCurrentMateInfo(User meOrMyMate);

  boolean existsPendingRequestBetweenUsers(User requester, User requestee);
}
