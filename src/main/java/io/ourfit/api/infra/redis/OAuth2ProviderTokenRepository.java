package io.ourfit.api.infra.redis;

import io.ourfit.api.domain.auth.data.entity.OAuth2ProviderToken;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2ProviderTokenRepository
    extends CrudRepository<OAuth2ProviderToken, String> {}
