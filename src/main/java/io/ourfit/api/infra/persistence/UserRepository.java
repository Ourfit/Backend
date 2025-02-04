package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.id = :id")
  @EntityGraph(attributePaths = {"favoriteWorkouts", "favoriteWorkoutPlaces"})
  Optional<User> findByIdWithFavorites(Long id);

  Optional<User> findByOAuthId(String oAuthId);

  boolean existsByOAuthId(String oAuthId);
}
