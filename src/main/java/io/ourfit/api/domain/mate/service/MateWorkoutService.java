package io.ourfit.api.domain.mate.service;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutPlacesUpsertDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutTimeUpsertDto;
import io.ourfit.api.domain.mate.data.entity.Mate;

public interface MateWorkoutService {

  void initialize(Mate mate);

  void putWorkoutPlaces(final long mateId, final MateWorkoutPlacesUpsertDto upsertDto);

  void putWorkoutTimes(final long mateId, final MateWorkoutTimeUpsertDto upsertDto);
}
