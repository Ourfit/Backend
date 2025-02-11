package io.ourfit.api.domain.user.data.dto.misc;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

public record KakaoUserTermsAgreementInfo(
    long id, @JsonProperty("service_terms") @Nullable ServiceTerms[] serviceTerms) {

  public record ServiceTerms(String tag, boolean required, boolean agreed, boolean revocable) {}
}
