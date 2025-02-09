package io.ourfit.api.domain.mate.data.dto.request;

import jakarta.validation.constraints.NotEmpty;

/**
 * 메이트와 즐겨찾는 운동 장소 등록/수정 요청 DTO
 *
 * @param placeName 장소 이름
 * @param address 주소
 */
public record MateWorkoutPlacesUpsertRequest(
    @NotEmpty String placeName, @NotEmpty String address) {}
