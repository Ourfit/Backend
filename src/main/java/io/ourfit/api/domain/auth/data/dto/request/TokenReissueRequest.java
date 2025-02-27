package io.ourfit.api.domain.auth.data.dto.request;

import jakarta.validation.constraints.NotEmpty;

/**
 * 토큰 재발급 요청 DTO
 *
 * @param accessToken 접근 토큰
 */
public record TokenReissueRequest(@NotEmpty String accessToken) {}
