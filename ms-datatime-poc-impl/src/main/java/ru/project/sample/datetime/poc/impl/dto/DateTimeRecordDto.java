package ru.project.sample.datetime.poc.impl.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class DateTimeRecordDto {
  private LocalDate ld;
  private LocalDateTime ldt;
  private OffsetDateTime odt;
  private OffsetDateTime odtz;
}
