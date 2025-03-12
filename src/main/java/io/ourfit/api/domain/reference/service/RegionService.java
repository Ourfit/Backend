package io.ourfit.api.domain.reference.service;

import io.ourfit.api.domain.reference.data.dto.internal.Places;
import io.ourfit.api.domain.reference.data.entity.Region;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.List;
import java.util.Optional;

public interface RegionService {

  List<Region> findAllRegionByKeyword(String keyword);

  Optional<Places> findPlacesByUserAndKeyword(User user, String keyword);

  boolean isValidRegion(String region1, String region2, String region3);
}
