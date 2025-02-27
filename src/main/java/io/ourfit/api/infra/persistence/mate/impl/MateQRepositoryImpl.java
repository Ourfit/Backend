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
  public Optional<MateInfoDto> findCurrentMateInfo(User meOrMyMate) {
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
                        qMate.myMate.id,
                        qMate.myMate.profileImageUrl,
                        qMate.myMate.nickname,
                        qMate.myMate.genderType,
                        qMate.myMate.age),
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
                qMate.me.eq(meOrMyMate).or(qMate.myMate.eq(meOrMyMate)))
            .fetchOne();

    return Optional.ofNullable(result);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsPendingRequestBetweenUsers(User requester, User requestee) {
    return this.queryFactory
            .selectOne()
            .from(qMate)
            .where(
                qMate.statusType.eq(MateStatusType.PENDING),
                qMate.me.eq(requester),
                qMate.myMate.eq(requestee))
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
