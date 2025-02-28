package io.ourfit.api.infra.persistence.mate.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ourfit.api.domain.mate.data.dto.internal.MateInfoDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutDto;
import io.ourfit.api.domain.mate.data.dto.internal.MyMateInfoDto;
import io.ourfit.api.domain.mate.data.entity.QMate;
import io.ourfit.api.domain.mate.data.entity.QMateWorkout;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.user.data.entity.QUser;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.infra.persistence.mate.MateQRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MateQRepositoryImpl implements MateQRepository {

  private static final QMate qMate = QMate.mate;
  private static final QMateWorkout qMateWorkout = QMateWorkout.mateWorkout;
  private static final QUser qMyMate = new QUser("myMate");

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<MateInfoDto> findCurrentMateInfo(User currentUser) {
    var result =
        this.queryFactory
            .select(
                Projections.constructor(
                    MateInfoDto.class,
                    qMate.id,
                    qMate.statusType,
                    getDaysSinceAccepted(),
                    Projections.constructor(
                        MyMateInfoDto.class,
                        Expressions.cases()
                            .when(qMate.me.eq(currentUser))
                            .then(qMate.myMate.id)
                            .otherwise(qMate.me.id),
                        Expressions.cases()
                            .when(qMate.me.eq(currentUser))
                            .then(qMate.myMate.profileImageUrl)
                            .otherwise(qMate.me.profileImageUrl),
                        Expressions.cases()
                            .when(qMate.me.eq(currentUser))
                            .then(qMate.myMate.nickname)
                            .otherwise(qMate.me.nickname),
                        Expressions.cases()
                            .when(qMate.me.eq(currentUser))
                            .then(qMate.myMate.genderType)
                            .otherwise(qMate.me.genderType),
                        Expressions.cases()
                            .when(qMate.me.eq(currentUser))
                            .then(qMate.myMate.age)
                            .otherwise(qMate.me.age)),
                    Projections.constructor(
                        MateWorkoutDto.class,
                        qMateWorkout.placeName,
                        qMateWorkout.address,
                        qMateWorkout.workoutDayOfWeek,
                        qMateWorkout.workoutStartAt,
                        qMateWorkout.workoutEndAt)))
            .from(qMate)
            .leftJoin(qMateWorkout)
            .on(qMateWorkout.mate.eq(qMate))
            .leftJoin(qMate.myMate, qMyMate)
            .where(
                qMate.statusType.eq(MateStatusType.MATCHED),
                qMate.me.eq(currentUser).or(qMate.myMate.eq(currentUser)))
            .fetchOne();

    return Optional.ofNullable(result);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsMateBetweenUsers(MateStatusType statusType, User user1, User user2) {
    return this.queryFactory
            .selectOne()
            .from(qMate)
            .where(
                qMate.statusType.eq(statusType),
                (qMate.me.eq(user1).and(qMate.myMate.eq(user2)))
                    .or(qMate.me.eq(user2).and(qMate.myMate.eq(user1))))
            .fetchFirst()
        != null;
  }

  private static Expression<Integer> getDaysSinceAccepted() {
    return Expressions.numberTemplate(
        Integer.class,
        "GREATEST(1, DATEDIFF(COALESCE({0}, NOW()), {1}) + 1)",
        qMate.deletedAt,
        qMate.acceptedAt);
  }
}
