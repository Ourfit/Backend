package io.ourfit.api.domain.user.service.impl;

import io.ourfit.api.domain.user.data.dto.internal.*;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.infra.persistence.UserQRepository;
import io.ourfit.api.infra.persistence.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

  private final UserRepository repository;
  private final UserQRepository qRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<UserInfoDto> findMateCandidates(
      MatesCandidateSearchDto searchDto, Pageable pageable) {
    return this.qRepository.findMateCandidates(searchDto, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findById(final long id) {
    return this.repository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByIdWithFavorites(final long id) {
    return this.repository.findByIdWithFavorites(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByOAuthId(String oAuthId) {
    return this.repository.findByoAuthId(oAuthId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByOAuthId(String oAuthId) {
    return this.repository.existsByoAuthId(oAuthId);
  }
}
