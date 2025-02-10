package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.user.data.dto.request.UserFavoritePlacesUpsertRequest;

/**
 * 사용자 선호 운동 시설(장소) 등록/수정 DTO
 *
 * @param placeName 장소명
 * @param address 주소
 */
public record UserFavoritePlacesUpsertDto(String placeName, String address) {

  public static UserFavoritePlacesUpsertDto fromRequest(UserFavoritePlacesUpsertRequest request) {
    return new UserFavoritePlacesUpsertDto(request.placeName(), request.address());
  }
}
