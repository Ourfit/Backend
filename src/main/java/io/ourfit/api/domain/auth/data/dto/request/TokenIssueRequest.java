package io.ourfit.api.domain.auth.data.dto.request;

import jakarta.validation.constraints.NotEmpty;

/**
 * 토큰 발급 요청 DTO
 *
 * @param oAuthId 토큰을 발급할 사용자의 OAuth ID
 */
public record TokenIssueRequest(@NotEmpty String code, @NotEmpty String oAuthId) {}
