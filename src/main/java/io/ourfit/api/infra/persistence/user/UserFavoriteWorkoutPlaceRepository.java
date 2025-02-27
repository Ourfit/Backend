package io.ourfit.api.infra.persistence.user;

import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkoutPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteWorkoutPlaceRepository
    extends JpaRepository<UserFavoriteWorkoutPlace, Long> {}
