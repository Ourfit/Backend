package io.ourfit.api.domain.auth.service;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;

public interface OAuth2Service {

  OAuth2UserInfo getUserInfo(String oAuthId);

  void withdrawal(String oAuthId);
}
