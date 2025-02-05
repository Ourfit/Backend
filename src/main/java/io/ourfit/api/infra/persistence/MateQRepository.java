package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.user.data.entity.User;

public interface MateQRepository {

  boolean existsPendingRequestBetweenUsers(User requester, User requestee);
}
