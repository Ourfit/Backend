package io.ourfit.api.infra.persistence.user;

import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkoutId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteWorkoutRepository
    extends JpaRepository<UserFavoriteWorkout, UserFavoriteWorkoutId> {}
