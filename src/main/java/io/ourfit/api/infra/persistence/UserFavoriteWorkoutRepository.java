package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkoutId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteWorkoutRepository
    extends JpaRepository<UserFavoriteWorkout, UserFavoriteWorkoutId> {}
