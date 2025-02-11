package io.ourfit.api.infra.persistence.mate;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.workout.data.enums.MateStatusType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MateRepository extends JpaRepository<Mate, Long> {

  Optional<Mate> findByIdAndStatusType(long mateId, MateStatusType status);

  @Query("SELECT m FROM Mate m WHERE m.me = :user OR m.myMate = :user AND m.statusType = :status")
  Optional<Mate> findByUserAndStatusType(User user, MateStatusType status);
}
