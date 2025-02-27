package io.ourfit.api.infra.persistence.user;

import io.ourfit.api.domain.user.data.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
  @EntityGraph(attributePaths = {"favoriteWorkouts", "favoriteWorkoutPlaces"})
  Optional<User> findByIdWithFavorites(Long id);

  Optional<User> findByoAuthId(String oAuthId);

  boolean existsByoAuthIdAndDeletedAtIsNull(String oAuthId);

  boolean existsByNicknameAndDeletedAtIsNull(String nickname);
}
