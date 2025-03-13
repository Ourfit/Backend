package io.ourfit.api.domain.reference.data.dto.internal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.ourfit.api.global.data.RedisSerializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 공휴일 정보 DTO
 *
 * @param header XML 헤더
 * @param body XML 본문
 */
@JacksonXmlRootElement(localName = "response")
public record Holidays(
    @JacksonXmlProperty(localName = "header") Header header,
    @JacksonXmlProperty(localName = "body") Body body)
    implements RedisSerializable {

  public List<Item> items() {
    return this.body.items.itemList();
  }

  /**
   * 응답의 헤더 정보
   *
   * @param resultCode 결과 코드
   * @param resultMsg 결과 메세지
   */
  public record Header(
      @JacksonXmlProperty(localName = "resultCode") String resultCode,
      @JacksonXmlProperty(localName = "resultMsg") String resultMsg) {}

  /**
   * 응답의 본문 정보
   *
   * @param items 공휴일 목록을 포함하는 {@link Items} 객체
   * @param numOfRows 요청된 데이터 수
   * @param pageNo 현재 페이지 번호
   * @param totalCount 총 데이터 수
   */
  public record Body(
      @JacksonXmlProperty(localName = "items") Items items,
      @JacksonXmlProperty(localName = "numOfRows") int numOfRows,
      @JacksonXmlProperty(localName = "pageNo") int pageNo,
      @JacksonXmlProperty(localName = "totalCount") int totalCount) {}

  /**
   * 공휴일 목록
   *
   * @param itemList 개별 공휴일 정보
   */
  public record Items(
      @JacksonXmlElementWrapper(useWrapping = false) @JacksonXmlProperty(localName = "item")
          List<Item> itemList) {}

  /**
   * 공휴일 정보
   *
   * @param dateKind 종류
   * @param dateName 이름 (e.g., 어린이날)
   * @param isHoliday 공공기관 휴일 여부
   * @param locdate 날짜 (yyyyMMdd)
   * @param seq 순번
   */
  public record Item(
      @JacksonXmlProperty(localName = "dateKind") String dateKind,
      @JacksonXmlProperty(localName = "dateName") String dateName,
      @JacksonXmlProperty(localName = "isHoliday") String isHoliday,
      @JacksonXmlProperty(localName = "locdate")
          @JsonFormat(pattern = "yyyyMMdd", timezone = "Asia/Seoul")
          LocalDate locdate,
      @JacksonXmlProperty(localName = "seq") int seq) {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String date() {
      return DATE_FORMAT.format(this.locdate);
    }
  }
}
