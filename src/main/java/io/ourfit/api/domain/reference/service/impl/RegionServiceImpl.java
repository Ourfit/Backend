package io.ourfit.api.domain.reference.service.impl;

import io.ourfit.api.domain.reference.data.entity.Region;
import io.ourfit.api.domain.reference.service.RegionService;
import io.ourfit.api.infra.client.http.KakaoLocalClient;
import io.ourfit.api.infra.persistence.reference.RegionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

  private final RegionRepository repository;
  private final KakaoLocalClient localClient;

  @Override
  public List<Region> findAllByKeyword(String keyword) {
    return this.repository.findAllByKeyword(keyword);
  }

  @Override
  public boolean isValidRegion(String region1, String region2, String region3) {
    return this.repository.existsByRegion1AndRegion2AndRegion3(region1, region2, region3);
  }
}
