package io.ourfit.api.infra.redis;

import io.ourfit.api.domain.auth.data.entity.OurfitRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface OurfitRefreshTokenRepository extends CrudRepository<OurfitRefreshToken, Long> {}
