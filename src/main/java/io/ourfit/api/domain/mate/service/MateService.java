package io.ourfit.api.domain.mate.service;

public interface MateService {

  void apply(final long requesterId, final long requesteeId);

  void accept(final long mateId);

  void delete(final long mateId);
}
