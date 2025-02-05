package io.ourfit.api.domain.mate.data.dto.internal;

import io.ourfit.api.domain.mate.data.dto.request.MateWorkoutPlacesUpsertRequest;

/**
 * 메이트와 선호 운동 시설(장소) 등록/수정 DTO
 *
 * @param placeName 장소명
 * @param address 주소
 */
public record MateWorkoutPlacesUpsertDto(String placeName, String address) {

  public static MateWorkoutPlacesUpsertDto fromRequest(MateWorkoutPlacesUpsertRequest request) {
    return new MateWorkoutPlacesUpsertDto(request.placeName(), request.address());
  }
}
