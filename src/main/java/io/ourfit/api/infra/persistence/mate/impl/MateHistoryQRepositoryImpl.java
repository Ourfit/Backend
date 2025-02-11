package io.ourfit.api.infra.persistence.mate.impl;

import static com.querydsl.core.types.ExpressionUtils.count;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;
import io.ourfit.api.domain.mate.data.entity.QMate;
import io.ourfit.api.domain.mate.data.entity.QMateHistory;
import io.ourfit.api.domain.user.data.entity.QUser;
import io.ourfit.api.infra.persistence.mate.MateHistoryQRepository;
import java.util.List;
import java.util.Optional;
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
  public Page<MateHistoryDto> findAllByUserId(final long userId, Pageable pageable) {
    List<MateHistoryDto> contents =
        this.queryFactory
            .select(
                Projections.constructor(
                    MateHistoryDto.class,
                    qMateHistory.id,
                    qMate.id,
                    qMateHistory.actionType,
                    isRead(userId),
                    qActor.id,
                    qActor.nickname,
                    qTarget.id,
                    qTarget.nickname,
                    qMateHistory.createdAt))
            .from(qMateHistory)
            .innerJoin(qMateHistory.mate, qMate)
            .innerJoin(qMateHistory.actor, qActor)
            .innerJoin(qMateHistory.target, qTarget)
            .where(qActor.id.eq(userId).or(qTarget.id.eq(userId)))
            .orderBy(qMateHistory.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    final long totalCount =
        Optional.of(this.queryFactory)
            .map(
                query ->
                    query
                        .select(count(qMateHistory))
                        .from(qMateHistory)
                        .where(qActor.id.eq(userId).or(qTarget.id.eq(userId)))
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
}
