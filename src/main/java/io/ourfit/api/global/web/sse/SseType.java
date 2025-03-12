package io.ourfit.api.global.web.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SseType {
  ALL,
  SYSTEM_ALERT
}
