package io.ourfit.api.global.converter;

import jakarta.persistence.Converter;
import java.time.DayOfWeek;

@Converter
public class DayOfWeekSetConverter extends AbstractSetConverter<DayOfWeek> {

  public DayOfWeekSetConverter(Class<DayOfWeek> enumClass) {
    super(enumClass);
  }
}
