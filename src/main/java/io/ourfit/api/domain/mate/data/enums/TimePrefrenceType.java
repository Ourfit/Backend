package io.ourfit.api.domain.mate.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimePrefrenceType {
  WEEKDAY_MORNING("평일 아침"),
  WEEKDAY_AFTERNOON("평일 오후"),
  WEEKDAY_EVENING("평일 저녁"),
  WEEKEND_MORNING("주말 아침"),
  WEEKEND_AFTERNOON("주말 오후"),
  WEEKEND_EVENING("주말 저녁");

  private final String description;
}
