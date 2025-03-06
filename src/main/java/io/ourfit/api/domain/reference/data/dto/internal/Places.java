package io.ourfit.api.domain.reference.data.dto.internal;

import static io.ourfit.api.infra.client.http.config.KakaoLocalClientConfig.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Arrays;

/**
 * 카카오 키워드로 장소 검색 응답 DTO
 *
 * @param documents 응답 결과
 * @param meta 응답 관련 정보
 */
public record Places(Document[] documents, Meta meta) {

  public Document[] sportFacilities() {
    return Arrays.stream(this.documents).filter(Document::isSportFacility).toArray(Document[]::new);
  }

  /**
   * 주소 → 좌표 전환 응답
   *
   * @param addressName 전체 지번 주소
   * @param categoryGroupCode 중요 카테고리만 그룹핑한 카테고리 그룹 코드
   * @param categoryGroupName 중요 카테고리만 그룹핑한 카테고리 그룹명
   * @param categoryName 카테고리 이름
   * @param distance 중심좌표까지의 거리 (x,y 파라미터를 준 경우에만, 단위 m)
   * @param id 장소 ID
   * @param placeName 장소명(업체명)
   * @param placeUrl 장소 상세페이지 URL
   * @param roadAddressName 전체 도로명 주소
   * @param longitude X 좌표값, 경위도인 경우 경도(longitude)
   * @param latitude Y 좌표값, 경위도인 경우 위도(latitude)
   */
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public record Document(
      String addressName,
      String categoryGroupCode,
      String categoryGroupName,
      String categoryName,
      String distance,
      String id,
      String placeName,
      String placeUrl,
      String roadAddressName,
      @JsonProperty("x") double longitude,
      @JsonProperty("y") double latitude) {

    public boolean isSportFacility() {
      if (this.categoryName == null || this.categoryName.isBlank()) {
        return false;
      }

      String[] categories = this.categoryName.split(CATEGORY_NAME_DELIMITER);

      if (categories.length < 2) {
        return false;
      }

      return TARGET_PLACE_CATEGORY_DEPTH_1.equals(categories[0].trim())
          && TARGET_PLACE_CATEGORY_DEPTH_2.equals(categories[1].trim());
    }
  }

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
