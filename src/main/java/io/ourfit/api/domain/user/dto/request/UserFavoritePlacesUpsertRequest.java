package io.ourfit.api.domain.user.dto.request;

import io.ourfit.api.domain.user.dto.internal.UserFavoritePlacesUpsertDto;

/**
 * 사용자 즐겨찾는 장소 등록/수정 요청 DTO
 *
 * @param placeName 장소 이름
 * @param address 주소
 */
public record UserFavoritePlacesUpsertRequest(String placeName, String address) {

  public UserFavoritePlacesUpsertDto toDto() {
    return new UserFavoritePlacesUpsertDto(this.placeName, this.address);
  }
}
