package io.ourfit.api.global.security.data;

import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

/** {@link AuditorAware}를 확장해 현재 인증된 사용자에 대한 Auditing 정보를 제공하는 인터페이스 */
public interface OurfitAuditorAware extends AuditorAware<Long> {

  String ANONYMOUS_USER = "anonymousUser";

  /**
   * 현재 사용자 정보를 가져온다.
   *
   * @return 현재 사용자 정보, 인증되지 않은 경우 {@link Optional#empty()}
   */
  Optional<User> getCurrentAuditorUser();
}
