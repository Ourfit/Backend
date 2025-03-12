package io.ourfit.api.infra.persistence.mate.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateHistorySearchDto;
import io.ourfit.api.domain.mate.data.entity.QMate;
import io.ourfit.api.domain.mate.data.entity.QMateHistory;
import io.ourfit.api.domain.mate.data.enums.MateActionType;
import io.ourfit.api.domain.mate.data.enums.MateRoleType;
import io.ourfit.api.domain.user.data.entity.QUser;
import io.ourfit.api.infra.persistence.mate.MateHistoryQRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MateHistoryQRepositoryImpl implements MateHistoryQRepository {

  private static final QMateHistory qMateHistory = QMateHistory.mateHistory;
  private static final QMate qMate = QMate.mate;
  private static final QUser qActor = new QUser("actor");
  private static final QUser qTarget = new QUser("target");

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional(readOnly = true)
  public Page<MateHistoryDto> findAllByUserId(
      final long userId, MateHistorySearchDto searchDto, Pageable pageable) {
    List<MateHistoryDto> contents =
        this.queryFactory
            .select(
                Projections.constructor(
                    MateHistoryDto.class,
                    qMateHistory.id,
                    qMate.id,
                    qMateHistory.actionType,
                    mateRoleType(userId),
                    isRead(userId),
                    qActor.id,
                    qActor.nickname,
                    qActor.profileImageUrl,
                    qTarget.id,
                    qTarget.nickname,
                    qTarget.profileImageUrl,
                    qMateHistory.createdAt))
            .from(qMateHistory)
            .innerJoin(qMateHistory.mate, qMate)
            .innerJoin(qMateHistory.actor, qActor)
            .innerJoin(qMateHistory.target, qTarget)
            .where(
                qActor.id.eq(userId).or(qTarget.id.eq(userId)),
                filterByActionType(userId, searchDto))
            .orderBy(qMateHistory.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    final long totalCount =
        Optional.of(this.queryFactory)
            .map(
                query ->
                    query
                        .select(qMateHistory.count())
                        .from(qMateHistory)
                        .where(
                            qActor.id.eq(userId).or(qTarget.id.eq(userId)),
                            filterByActionType(userId, searchDto))
                        .fetchOne())
            .orElse(0L);

    return new PageImpl<>(contents, pageable, totalCount);
  }

  private static Expression<Boolean> isRead(long userId) {
    return Expressions.cases()
        .when(qMateHistory.actor.id.eq(userId))
        .then((ComparableExpression<Boolean>) qMateHistory.actorRead)
        .when(qMateHistory.target.id.eq(userId))
        .then(qMateHistory.targetRead)
        .otherwise(false);
  }

  private static Expression<String> mateRoleType(long userId) {
    return Expressions.cases()
        .when(qMateHistory.actor.id.eq(userId))
        .then(Expressions.constant(MateRoleType.ACTOR.name()))
        .when(qMateHistory.target.id.eq(userId))
        .then(Expressions.constant(MateRoleType.TARGET.name()))
        .otherwise(Expressions.constant(MateRoleType.ACTOR.name()));
  }

  private static BooleanExpression filterByActionType(long userId, MateHistorySearchDto searchDto) {
    Set<MateActionType> actionTypes = searchDto.actionTypes();
    if (actionTypes == null || actionTypes.isEmpty()) {
      return null;
    }
    if (actionTypes.contains(MateActionType.RECEIVE)) {
      actionTypes.remove(MateActionType.RECEIVE);
      var receivedRequestCondition =
          qMateHistory.target.id.eq(userId).and(qMateHistory.actionType.eq(MateActionType.APPLY));
      return qMateHistory.actionType.in(actionTypes).or(receivedRequestCondition);
    }
    if (actionTypes.contains(MateActionType.APPLY)) {
      actionTypes.remove(MateActionType.APPLY);
      var sentRequestCondition =
          qMateHistory.actor.id.eq(userId).and(qMateHistory.actionType.eq(MateActionType.APPLY));
      return qMateHistory.actionType.in(actionTypes).or(sentRequestCondition);
    }
    // orElse
    return qMateHistory.actionType.in(actionTypes);
  }
}
