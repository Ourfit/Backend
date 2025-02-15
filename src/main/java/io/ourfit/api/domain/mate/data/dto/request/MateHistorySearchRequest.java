package io.ourfit.api.domain.mate.data.dto.request;

import io.ourfit.api.domain.workout.data.enums.MateActionType;
import io.ourfit.api.global.web.validation.Enumerable;
import java.util.Set;

/**
 * 메이트 이력 검색 요청 DTO
 *
 * @param actionTypes 검색할 메이트 액션 타입
 */
public record MateHistorySearchRequest(
    @Enumerable(type = MateActionType.class, required = false) Set<String> actionTypes) {}
