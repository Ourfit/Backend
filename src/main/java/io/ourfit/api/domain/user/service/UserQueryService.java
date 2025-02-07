package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.data.dto.internal.*;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryService {

  Page<UserInfoDto> findMateCandidates(MatesCandidateSearchDto searchDto, Pageable pageable);

  Optional<User> findById(final long id);

  Optional<User> findByIdWithFavorites(final long id);

  Optional<User> findByOAuthId(String oAuthId);

  boolean existsByOAuthId(String oAuthId);
}
