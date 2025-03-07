package io.ourfit.api.global.security.userdetails;

import io.ourfit.api.domain.user.data.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface OurfitUserDetails extends UserDetails {

  User getUser();

  Long getId();

  boolean isSuperAdmin();

  boolean isAdmin();

  static OurfitUserDetails from(User user) {
    return new OurfitUserDetailsImpl(user);
  }
}
