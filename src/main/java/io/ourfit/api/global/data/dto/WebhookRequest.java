package io.ourfit.api.global.data.dto;

/**
 * Discord Webhook 요청 DTO
 *
 * @param content 메시지 내용
 */
public record WebhookRequest(String content) {}
