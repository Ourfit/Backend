package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = {"favoriteWorkouts", "favoriteWorkoutPlaces"})
  Optional<User> findByIdWithFavorites(Long id);

  Optional<User> findByEmail(String email);
}
