package io.ourfit.api.global.security.userdetails;

import io.ourfit.api.domain.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface OurfitUserDetails extends UserDetails {

  User getUser();

  Long getId();

  boolean isSuperAdmin();

  boolean isAdmin();
}
