package io.ourfit.api.global.security.data;

import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class OurfitAuditorAwareImpl implements OurfitAuditorAware {
  /**
   * Spring Security의 {@link SecurityContextHolder}를 사용하여 현재 사용자 정보를 가져온다. <br>
   * 인증 정보가 없거나, 인증되지 않은 경우 Optional.empty()를 반환한다. <br>
   * 현재 컨텍스트에서 인증 정보를 가져오므로, 컨텍스트가 전파되지 않는 작업<i>(e.g. @Async)</i>에서는 사용이 불가능할 수 있음!
   *
   * @return 현재 사용자 정보
   */
  @Override
  public Optional<Long> getCurrentAuditor() {
    return this.getCurrentUserDetails().map(OurfitUserDetails::getId);
  }

  @Override
  public Optional<User> getCurrentAuditorUser() {
    return this.getCurrentUserDetails().map(OurfitUserDetails::getUser);
  }

  private boolean isNotAnonymous(Authentication authentication) {
    return !ANONYMOUS_USER.equalsIgnoreCase(authentication.getName());
  }

  private Optional<OurfitUserDetails> getCurrentUserDetails() {
    return Optional.of(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .filter(this::isNotAnonymous)
        .map(Authentication::getPrincipal)
        .map(OurfitUserDetails.class::cast);
  }
}
