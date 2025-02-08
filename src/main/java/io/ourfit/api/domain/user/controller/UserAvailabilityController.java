package io.ourfit.api.domain.user.controller;

import static io.ourfit.api.domain.user.validation.impl.NicknameValidator.NICKNAME_PATTERN;

import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/check-availability")
public class UserAvailabilityController {

  private final UserQueryService queryService;

  @GetMapping
  public ResponseEntity<Void> checkAvailability(
      @RequestParam String field, @RequestParam String value) {
    var availabilityType = UserAvailabilityType.valueOf(field.toUpperCase());

    if (availabilityType.isInvalidInput(value)) {
      return ResponseEntity.badRequest().build();
    }

    final boolean result =
        switch (availabilityType) {
          case NICKNAME -> this.queryService.existsByNickname(value);
          case OAUTHID -> this.queryService.existsByOAuthId(value);
        };

    HttpStatus httpStatus = getHttpStatus(result);
    return ResponseEntity.status(httpStatus).build();
  }

  private static HttpStatus getHttpStatus(boolean result) {
    return result ? HttpStatus.CONFLICT : HttpStatus.NO_CONTENT;
  }

  @AllArgsConstructor
  private enum UserAvailabilityType {
    NICKNAME(value -> NICKNAME_PATTERN.matcher(value).matches()),
    OAUTHID(StringUtils::hasText);

    private final Predicate<String> validator;

    public boolean isInvalidInput(final String value) {
      return !this.validator.test(value);
    }
  }
}
