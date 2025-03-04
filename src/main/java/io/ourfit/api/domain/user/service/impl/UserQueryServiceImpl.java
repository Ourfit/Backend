package io.ourfit.api.domain.user.service.impl;

import io.ourfit.api.domain.user.data.dto.internal.*;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.data.entity.User_;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.infra.persistence.user.UserQRepository;
import io.ourfit.api.infra.persistence.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

  private final UserRepository repository;
  private final UserQRepository qRepository;

  @Override
  public Page<UserInfoDto> findMateCandidates(
      User requestedUser, MateCandidateSearchDto searchDto, Pageable pageable) {
    return this.qRepository.findMateCandidates(requestedUser, searchDto, pageable);
  }

  @Override
  @Cacheable(value = "USER_INFO", key = "#id", unless = "#result.isEmpty()")
  public Optional<User> findById(long id) {
    return this.findById(id, false);
  }

  @Override
  @Cacheable(
      value = "USER_INFO",
      key = "#id",
      unless = "#result.isEmpty() && #includeDeleted = false")
  public Optional<User> findById(final long id, final boolean includeDeleted) {
    return this.repository.findOne(Specification.allOf(id(id), isDeleted(includeDeleted)));
  }

  @Override
  @Cacheable(value = "USER_DTL_INFO", key = "#id", unless = "#result.isEmpty()")
  public Optional<User> findByIdWithFavorites(final long id) {
    return this.repository.findByIdWithFavorites(id);
  }

  @Override
  @Cacheable(value = "USER_INFO", key = "#result.orElse(null)?.id", unless = "#result.isEmpty()")
  public Optional<User> findByOAuthId(String oAuthId) {
    return this.repository.findOne(Specification.allOf(oAuthId(oAuthId), isDeleted(false)));
  }

  @Override
  public boolean existsByOAuthId(String oAuthId) {
    return this.repository.exists(Specification.allOf(oAuthId(oAuthId), isDeleted(false)));
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return this.repository.exists(Specification.allOf(nickname(nickname), isDeleted(false)));
  }

  private static Specification<User> id(final long id) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.id), id);
  }

  private static Specification<User> oAuthId(final String oAuthId) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get(User_.oAuthId), oAuthId);
  }

  private static Specification<User> nickname(final String nickname) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get(User_.nickname), nickname);
  }

  private static Specification<User> isDeleted(final boolean includeDeleted) {
    return (root, query, criteriaBuilder) ->
        includeDeleted
            ? criteriaBuilder.conjunction()
            : criteriaBuilder.isNull(root.get(User_.deletedAt));
  }
}
