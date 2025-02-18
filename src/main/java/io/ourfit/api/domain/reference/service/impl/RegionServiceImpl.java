package io.ourfit.api.domain.reference.service.impl;

import io.ourfit.api.domain.reference.data.entity.Region;
import io.ourfit.api.domain.reference.service.RegionService;
import io.ourfit.api.infra.persistence.reference.RegionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

  private final RegionRepository repository;

  @Override
  @Transactional(readOnly = true)
  public List<Region> findAllByKeyword(String keyword) {
    return this.repository.findAllByKeyword(keyword);
  }
}
