package io.ourfit.api.global.persistence.converter;

import jakarta.persistence.Converter;
import java.time.DayOfWeek;

@Converter
public class DayOfWeekSetConverter extends AbstractSetConverter<DayOfWeek> {

  public DayOfWeekSetConverter() {
    super(DayOfWeek.class);
  }
}
