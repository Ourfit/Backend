package io.ourfit.api.domain.user.data.dto.request;

/**
 * 사용자 즐겨찾는 장소 등록/수정 요청 DTO
 *
 * @param placeName 장소 이름
 * @param address 주소
 */
public record UserFavoritePlacesUpsertRequest(String placeName, String address) {}
