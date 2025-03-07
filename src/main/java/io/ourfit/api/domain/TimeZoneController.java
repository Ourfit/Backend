package io.ourfit.api.domain;

import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
@RestController
@RequiredArgsConstructor
public class TimeZoneController {

  private final Clock clock;

  @GetMapping("/timezones")
  public ResponseEntity<Object> getTimeZone() {
    return ResponseEntity.ok(
        Map.of(
            "Default Zone ID",
            ZoneId.systemDefault(),
            "Default TimeZone",
            TimeZone.getDefault(),
            "Clock",
            clock.getZone(),
            "Now Test",
            LocalDateTime.now(),
            "Now Test2",
            LocalDateTime.now(clock),
            "CurrentTimeProvider",
            CurrentDateTimeProvider.INSTANCE.getNow().get()));
  }
}
