package io.ourfit.api.domain.auth.service.impl;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.auth.template.OAuth2TemplateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

  private final OAuth2TemplateFactory oAuth2TemplateFactory;

  @Override
  public OAuth2UserInfo getUserInfo(final String oAuthId) {
    return this.oAuth2TemplateFactory.getByOAuthId(oAuthId).getUserInfo(oAuthId);
  }

  @Override
  public void withdrawal(String oAuthId) {
    this.oAuth2TemplateFactory.getByOAuthId(oAuthId).withdrawal(oAuthId);
  }
}
