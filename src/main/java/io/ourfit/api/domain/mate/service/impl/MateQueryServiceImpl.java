package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.dto.internal.MateInfoDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.Mate_;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.mate.service.MateQueryService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.infra.persistence.mate.MateQRepository;
import io.ourfit.api.infra.persistence.mate.MateRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MateQueryServiceImpl implements MateQueryService {

  private final MateRepository repository;
  private final MateQRepository qRepository;

  @Override
  public Optional<Mate> findByIdAndStatus(final long mateId, MateStatusType status) {
    return this.repository.findOne(Specification.allOf(idEq(mateId), statusTypeEq(status)));
  }

  @Override
  public Optional<Mate> findByUserAndStatus(User user, MateStatusType status) {
    return this.repository.findOne(Specification.allOf(isMeOrMyMate(user), statusTypeEq(status)));
  }

  @Override
  public Optional<MateInfoDto> findCurrentMateInfo(User currentUser) {
    return this.qRepository.findCurrentMateInfo(currentUser);
  }

  private static Specification<Mate> idEq(long mateId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Mate_.id), mateId);
  }

  private static Specification<Mate> isMeOrMyMate(User user) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.or(
            criteriaBuilder.equal(root.get(Mate_.me), user),
            criteriaBuilder.equal(root.get(Mate_.myMate), user));
  }

  private static Specification<Mate> statusTypeEq(MateStatusType status) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get(Mate_.statusType), status);
  }
}
