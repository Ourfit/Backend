package io.ourfit.api.domain.reference.data.dto.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 카카오 주소를 좌표로 변환 응답 DTO
 *
 * @param documents 응답 결과
 * @param meta 응답 관련 정보
 */
public record AddressCoordinates(Document[] documents, Meta meta) {

  /**
   * 주소 → 좌표 변환 응답
   *
   * @param addressName 전체 지번 주소 또는 전체 도로명 주소
   * @param addressType 값의 타입 / REGION(지명), ROAD(도로명), REGION_ADDR(지번 주소), ROAD_ADDR(도로명 주소)
   * @param roadAddress 도로명 주소 상세 정보
   * @param longitude X 좌표값, 경위도인 경우 경도(longitude)
   * @param latitude Y 좌표값, 경위도인 경우 위도(latitude)
   */
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Document(
      String addressName,
      String addressType,
      String roadAddress,
      @JsonProperty("x") double longitude,
      @JsonProperty("y") double latitude) {}

  /**
   * 카카오 API의 응답 관련 정보
   *
   * @param isEnd 현재 페이지가 마지막 페이지인지 여부
   * @param pageableCount {@code total_count} 중 노출 가능 문서 수 (최대: {@code 45})
   * @param totalCount 검색어에 검색된 문서 수
   */
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Meta(boolean isEnd, int pageableCount, int totalCount) {}
}
