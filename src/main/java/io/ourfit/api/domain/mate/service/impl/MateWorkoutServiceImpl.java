package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutPlacesUpsertDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutTimeUpsertDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.MateWorkout;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.infra.persistence.mate.MateWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MateWorkoutServiceImpl implements MateWorkoutService {

  private final MateWorkoutRepository repository;

  @Override
  public void initialize(Mate mate) {
    this.repository.save(MateWorkout.ofEmpty(mate));
  }

  @Override
  public void putWorkoutPlaces(final long mateId, MateWorkoutPlacesUpsertDto upsertDto) {
    this.repository
        .findById(mateId)
        .ifPresentOrElse(
            mateWorkout -> mateWorkout.putWorkoutPlaces(upsertDto),
            () -> {
              throw new NoSuchEntityException();
            });
  }

  @Override
  public void putWorkoutTimes(final long mateId, MateWorkoutTimeUpsertDto upsertDto) {
    this.repository
        .findById(mateId)
        .ifPresentOrElse(
            mateWorkout -> mateWorkout.putWorkoutTimes(upsertDto),
            () -> {
              throw new NoSuchEntityException();
            });
  }
}
