package io.ourfit.api.domain.reference.data.dto.response;

/**
 * 장소 정보 응답 DTO
 *
 * @param addressName 전체 지번 주소
 * @param roadAddressName 전체 도로명 주소
 * @param placeName 장소(업체)명
 */
public record PlaceResponse(String addressName, String roadAddressName, String placeName) {}
