package io.ourfit.api.infra.persistence.impl;

import static com.querydsl.core.group.GroupBy.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Collections;
import io.ourfit.api.domain.mate.data.entity.QMate;
import io.ourfit.api.domain.user.data.dto.internal.MatesCandidateSearchDto;
import io.ourfit.api.domain.user.data.dto.internal.UserFavoriteWorkoutDto;
import io.ourfit.api.domain.user.data.dto.internal.UserInfoDto;
import io.ourfit.api.domain.user.data.entity.QUser;
import io.ourfit.api.domain.user.data.entity.association.QUserFavoriteWorkout;
import io.ourfit.api.domain.workout.data.entity.QWorkout;
import io.ourfit.api.domain.workout.data.enums.MateStatusType;
import io.ourfit.api.domain.workout.data.enums.TimePrefrenceType;
import io.ourfit.api.global.utils.QueryUtils;
import io.ourfit.api.infra.persistence.UserQRepository;
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
public class UserQRepositoryImpl implements UserQRepository {

  private static final QUser qUser = QUser.user;
  private static final QUserFavoriteWorkout favoriteWorkout =
      QUserFavoriteWorkout.userFavoriteWorkout;
  private static final QWorkout qWorkout = QWorkout.workout;
  private static final QMate qMate = QMate.mate;

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional(readOnly = true)
  public Page<UserInfoDto> findMateCandidates(
      MatesCandidateSearchDto searchDto, Pageable pageable) {
    List<UserInfoDto> contents =
        QueryUtils.toList(
            this.queryFactory
                .from(qUser)
                .leftJoin(favoriteWorkout)
                .on(favoriteWorkout.user.eq(qUser))
                .leftJoin(qWorkout)
                .on(favoriteWorkout.workout.eq(qWorkout))
                .where(
                    this.region3Eqauls(searchDto),
                    this.isNotMatchedWithMate(),
                    this.preferredTimesIn(searchDto),
                    this.workoutsIn(searchDto))
                .transform(
                    groupBy(qUser.id)
                        .as(
                            Projections.constructor(
                                UserInfoDto.class,
                                qUser.id,
                                qUser.profileImageUrl,
                                qUser.nickName,
                                qUser.genderType,
                                qUser.age,
                                qUser.skillLevelType,
                                qUser.introduction,
                                qUser.preferredWorkoutTime,
                                list(
                                    Projections.constructor(
                                        UserFavoriteWorkoutDto.class,
                                        qWorkout.code,
                                        qWorkout.name))))));

    final long totalCount =
        Optional.of(this.queryFactory)
            .map(
                query ->
                    query
                        .select(qUser.count())
                        .from(qUser)
                        .where(
                            this.region3Eqauls(searchDto),
                            this.isNotMatchedWithMate(),
                            this.preferredTimesIn(searchDto),
                            this.workoutsIn(searchDto))
                        .fetchOne())
            .orElse(0L);

    return new PageImpl<>(contents, pageable, totalCount);
  }

  private BooleanExpression region3Eqauls(MatesCandidateSearchDto searchDto) {
    return qUser.region3.eq(searchDto.region3());
  }

  private BooleanExpression isNotMatchedWithMate() {
    return JPAExpressions.selectOne()
        .from(qMate)
        .where(
            qMate.me.eq(qUser).or(qMate.myMate.eq(qUser)),
            qMate.statusType.eq(MateStatusType.MATCHED))
        .notExists();
  }

  private BooleanExpression preferredTimesIn(MatesCandidateSearchDto searchDto) {
    List<TimePrefrenceType> preferredTimes = searchDto.preferredTimes();
    if (Collections.isEmpty(preferredTimes)) {
      return null;
    }
    return qUser.preferredWorkoutTime.in(preferredTimes);
  }

  private BooleanExpression workoutsIn(MatesCandidateSearchDto searchDto) {
    Set<String> workoutCodes = searchDto.workoutTypes();
    if (Collections.isEmpty(workoutCodes)) {
      return null;
    }
    return favoriteWorkout.workout.code.in(workoutCodes);
  }
}
