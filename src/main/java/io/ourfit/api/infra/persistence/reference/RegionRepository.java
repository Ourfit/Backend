package io.ourfit.api.infra.persistence.reference;

import io.ourfit.api.domain.reference.data.entity.Region;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepository extends JpaRepository<Region, Long> {

  @Query(
      value =
          "SELECT * FROM region r WHERE MATCH(region1, region2, region3) AGAINST (:keyword IN BOOLEAN MODE) LIMIT 50",
      nativeQuery = true)
  List<Region> findAllByKeyword(String keyword);

  boolean existsByRegion1AndRegion2AndRegion3(String region1, String region2, String region3);
}
