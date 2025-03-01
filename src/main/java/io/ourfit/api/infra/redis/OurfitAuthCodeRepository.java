package io.ourfit.api.infra.redis;

import io.ourfit.api.domain.auth.data.entity.OurfitAuthCode;
import org.springframework.data.repository.CrudRepository;

public interface OurfitAuthCodeRepository extends CrudRepository<OurfitAuthCode, String> {}
