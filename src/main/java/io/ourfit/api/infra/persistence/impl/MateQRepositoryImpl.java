package io.ourfit.api.infra.persistence.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ourfit.api.domain.mate.QMate;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.enums.MateStatusType;
import io.ourfit.api.infra.persistence.MateQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MateQRepositoryImpl implements MateQRepository {

  private static final QMate qMate = QMate.mate;

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional(readOnly = true)
  public boolean existsPendingRequestBetweenUsers(User requester, User requestee) {
    return this.queryFactory
            .selectOne()
            .from(qMate)
            .where(
                qMate.statusType.eq(MateStatusType.PENDING),
                qMate.requester.eq(requester),
                qMate.requestee.eq(requestee))
            .fetchFirst()
        != null;
  }
}
