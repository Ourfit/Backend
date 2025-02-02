package io.ourfit.api.domain.auth.service.impl;

import io.ourfit.api.domain.auth.data.dto.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.auth.template.OAuth2Template;
import io.ourfit.api.domain.auth.template.OAuth2TemplateFactory;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

  private final OAuth2TemplateFactory oAuth2TemplateFactory;

  @Override
  public OAuth2UserInfo authenticate(OAuth2ProviderType providerType, String code) {
    OAuth2Template selectedTemplate = this.oAuth2TemplateFactory.getByProviderType(providerType);

    final String oAuthId = selectedTemplate.issueToken(code).getId();
    return selectedTemplate.getUserInfo(oAuthId);
  }
}
