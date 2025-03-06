package io.ourfit.api.domain.reference.data.dto.response;

import io.ourfit.api.domain.reference.data.dto.internal.Places;

/**
 * 장소 정보 응답 DTO
 *
 * @param addressName 전체 지번 주소
 * @param roadAddressName 전체 도로명 주소
 * @param placeName 장소(업체)명
 * @param distance 기준 좌표(사용자 지역)으로부터의 거리 (단위: m)
 */
public record PlaceResponse(
    String addressName, String roadAddressName, String placeName, int distance) {

  public static PlaceResponse from(Places.Document document) {
    return new PlaceResponse(
        document.addressName(),
        document.roadAddressName(),
        document.placeName(),
        Integer.parseInt(document.distance()));
  }
}
