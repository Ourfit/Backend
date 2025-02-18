package io.ourfit.api.domain.reference.data.dto.response;

import io.ourfit.api.domain.reference.data.entity.Region;

/**
 * 지역 정보 응답 DTO
 *
 * @param fullName 전체 지역명
 * @param region1 지역1(시/도)
 * @param region2 지역2(시/군/구)
 * @param region3 지역3(읍/면/동)
 */
public record RegionResponse(String fullName, String region1, String region2, String region3) {

  public static RegionResponse from(Region region) {
    return new RegionResponse(
        region.getFullName(), region.getRegion1(), region.getRegion2(), region.getRegion3());
  }
}
