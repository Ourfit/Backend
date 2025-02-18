package io.ourfit.api.domain.mate.data.dto.internal;

import io.ourfit.api.domain.mate.data.dto.request.MateHistorySearchRequest;
import io.ourfit.api.domain.mate.data.enums.MateActionType;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.Set;

/**
 * 메이트 히스토리 검색 DTO
 *
 * @param actionTypes 액션 타입
 */
public record MateHistorySearchDto(Set<MateActionType> actionTypes) {

  public static MateHistorySearchDto fromRequest(MateHistorySearchRequest request) {
    return new MateHistorySearchDto(
        StreamUtils.mapToSet(request.actionTypes(), MateActionType::findByName));
  }
}
