package io.ourfit.api.infra.persistence;

import static com.querydsl.core.group.GroupBy.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Collections;
import io.ourfit.api.domain.user.data.dto.internal.UserFavoriteWorkoutDto;
import io.ourfit.api.domain.user.data.dto.internal.UserInfoDto;
import io.ourfit.api.domain.user.data.dto.internal.UserSearchDto;
import io.ourfit.api.domain.user.entity.QUser;
import io.ourfit.api.domain.user.entity.association.QUserFavoriteWorkout;
import io.ourfit.api.domain.workout.QWorkout;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import io.ourfit.api.global.utils.QueryUtils;
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

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional(readOnly = true)
  public Page<UserInfoDto> findAllByConditions(UserSearchDto searchDto, Pageable pageable) {
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
                            this.preferredTimesIn(searchDto),
                            this.workoutsIn(searchDto))
                        .fetchOne())
            .orElse(0L);

    return new PageImpl<>(contents, pageable, totalCount);
  }

  private BooleanExpression region3Eqauls(UserSearchDto searchDto) {
    return qUser.region3.eq(searchDto.region3());
  }

  private BooleanExpression preferredTimesIn(UserSearchDto searchDto) {
    List<TimePrefrenceType> preferredTimes = searchDto.preferredTimes();
    if (Collections.isEmpty(preferredTimes)) {
      return null;
    }
    return qUser.preferredWorkoutTime.in(preferredTimes);
  }

  private BooleanExpression workoutsIn(UserSearchDto searchDto) {
    Set<String> workoutCodes = searchDto.workoutTypes();
    if (Collections.isEmpty(workoutCodes)) {
      return null;
    }
    return favoriteWorkout.workout.code.in(workoutCodes);
  }
}
