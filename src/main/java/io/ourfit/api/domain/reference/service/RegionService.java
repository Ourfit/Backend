package io.ourfit.api.domain.reference.service;

import io.ourfit.api.domain.reference.data.entity.Region;
import java.util.List;

public interface RegionService {

  List<Region> findAllByKeyword(String keyword);
}
