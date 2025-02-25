package io.ourfit.api.infra.persistence.user.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Collections;
import io.ourfit.api.domain.mate.data.entity.QMate;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import io.ourfit.api.domain.mate.data.enums.TimePrefrenceType;
import io.ourfit.api.domain.user.data.dto.internal.MateCandidateSearchDto;
import io.ourfit.api.domain.user.data.dto.internal.UserFavoriteWorkoutDto;
import io.ourfit.api.domain.user.data.dto.internal.UserInfoDto;
import io.ourfit.api.domain.user.data.entity.QUser;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.data.entity.association.QUserFavoriteWorkout;
import io.ourfit.api.domain.workout.data.entity.QWorkout;
import io.ourfit.api.global.utils.QueryUtils;
import io.ourfit.api.infra.persistence.user.UserQRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserQRepositoryImpl implements UserQRepository {

  private static final QUser qUser = QUser.user;
  private static final QUserFavoriteWorkout qFavoriteWorkout =
      QUserFavoriteWorkout.userFavoriteWorkout;
  private static final QWorkout qWorkout = QWorkout.workout;
  private static final QMate qMate = QMate.mate;

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional(readOnly = true)
  public Slice<UserInfoDto> findMateCandidates(
      User requestedUser, MateCandidateSearchDto searchDto, Pageable pageable) {
    var hasNext = false;
    var userIds = this.findMateCandidatesIds(requestedUser, searchDto, pageable);

    if (userIds.isEmpty()) {
      return Page.empty(pageable);
    }

    if (QueryUtils.hasNext(userIds, pageable.getPageSize())) {
      userIds.remove(userIds.size() - 1);
      hasNext = true;
    }

    var contents = this.fetchUserInfos(userIds);
    //    final long totalCount = this.countMateCandidates(requestedUser, searchDto);

    return new SliceImpl<>(contents, pageable, hasNext);
  }

  private List<Long> findMateCandidatesIds(
      User requestedUser, MateCandidateSearchDto searchDto, Pageable pageable) {
    return this.queryFactory
        .select(qUser.id)
        .from(qUser)
        .where(
            qUser.ne(requestedUser),
            qUser.deletedAt.isNull(),
            isNotMatchedWithMate(),
            region3Eqauls(searchDto),
            genderEquals(searchDto),
            preferredTimesIn(searchDto),
            workoutsIn(searchDto))
        .orderBy(qUser.createdAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();
  }

  private List<UserInfoDto> fetchUserInfos(List<Long> userIds) {
    return this.queryFactory
        .from(qUser)
        .leftJoin(qFavoriteWorkout)
        .on(qFavoriteWorkout.user.eq(qUser))
        .leftJoin(qWorkout)
        .on(qFavoriteWorkout.workout.eq(qWorkout))
        .where(qUser.id.in(userIds))
        .transform(
            groupBy(qUser.id)
                .list(
                    Projections.constructor(
                        UserInfoDto.class,
                        qUser.id,
                        qUser.profileImageUrl,
                        qUser.nickname,
                        qUser.genderType,
                        qUser.age,
                        qUser.skillLevelType,
                        qUser.introduction,
                        qUser.preferredWorkoutTime,
                        list(
                            Projections.constructor(
                                UserFavoriteWorkoutDto.class,
                                qFavoriteWorkout.workout.code,
                                qFavoriteWorkout.workout.name)))));
  }

  private long countMateCandidates(User requestedUser, MateCandidateSearchDto searchDto) {
    return Optional.of(this.queryFactory)
        .map(
            query ->
                query
                    .select(qUser.countDistinct())
                    .from(qUser)
                    .leftJoin(qFavoriteWorkout)
                    .on(qFavoriteWorkout.user.eq(qUser))
                    .leftJoin(qWorkout)
                    .on(qFavoriteWorkout.workout.eq(qWorkout))
                    .where(
                        qUser.ne(requestedUser),
                        qUser.deletedAt.isNull(),
                        isNotMatchedWithMate(),
                        region3Eqauls(searchDto),
                        genderEquals(searchDto),
                        preferredTimesIn(searchDto),
                        workoutsIn(searchDto))
                    .fetchOne())
        .orElse(0L);
  }

  private static BooleanExpression isNotMatchedWithMate() {
    return JPAExpressions.selectOne()
        .from(qMate)
        .where(
            qMate.me.eq(qUser).or(qMate.myMate.eq(qUser)),
            qMate.statusType.eq(MateStatusType.MATCHED))
        .notExists();
  }

  private static BooleanExpression region3Eqauls(MateCandidateSearchDto searchDto) {
    return qUser.region3.eq(searchDto.region3());
  }

  private static BooleanExpression genderEquals(MateCandidateSearchDto searchDto) {
    if (searchDto.gender() == null) {
      return null;
    }
    return qUser.genderType.eq(searchDto.gender());
  }

  private static BooleanExpression preferredTimesIn(MateCandidateSearchDto searchDto) {
    List<TimePrefrenceType> preferredTimes = searchDto.preferredTimes();
    if (preferredTimes == null || preferredTimes.isEmpty()) {
      return null;
    }
    return qUser.preferredWorkoutTime.in(preferredTimes);
  }

  private static BooleanExpression workoutsIn(MateCandidateSearchDto searchDto) {
    Set<String> workoutCodes = searchDto.workoutTypes();
    if (Collections.isEmpty(workoutCodes)) {
      return null;
    }
    return qUser.id.in(
        JPAExpressions.select(qFavoriteWorkout.user.id)
            .from(qFavoriteWorkout)
            .join(qWorkout)
            .on(qFavoriteWorkout.workout.eq(qWorkout))
            .where(qWorkout.code.in(searchDto.workoutTypes())));
  }
}
