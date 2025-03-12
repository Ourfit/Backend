package io.ourfit.api.global.web.hook.impl;

import io.ourfit.api.global.config.properties.WebhookProperties;
import io.ourfit.api.global.web.hook.WebhookRequest;
import io.ourfit.api.global.web.hook.WebhookService;
import io.ourfit.api.infra.client.http.DiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscordWebhookService implements WebhookService {

  private final DiscordClient discordClient;
  private final WebhookProperties webhookProperties;

  @Override
  public void send(String message) {
    WebhookProperties.Discord discordProperties = this.webhookProperties.discord();
    this.discordClient.send(
        discordProperties.serverId(), discordProperties.token(), new WebhookRequest(message));
  }
}
