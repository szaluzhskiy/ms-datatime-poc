package ru.project.sample.datetime.poc.impl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.project.sample.datetime.poc.impl.dto.DateTimeRecordDto;

@RestController("/dates")
@RequiredArgsConstructor
public class TestController {

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper mapper;

  @PostMapping("/internal")
  @Transactional
  public void createDates() {
    String insertTimestampQuery = "INSERT INTO test_datatime_table (ts_naked, ts_tz, ts_str, comment) VALUES (?,?,?,?)";

    Date initialDate = new Date();

    //local date
    LocalDate ld = initialDate.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

    Object[] params = { ld, ld, ld.toString(), "local date" };

    jdbcTemplate.update(insertTimestampQuery, params);
    //local date time
    LocalDateTime ldt = LocalDateTime.ofInstant(initialDate.toInstant(),
        ZoneId.systemDefault());

    Object[] paramsLdt = { ldt, ldt, ldt.toString(), "local date time" };

    jdbcTemplate.update(insertTimestampQuery, paramsLdt);

    //Offset date time MSK
    OffsetDateTime odt = OffsetDateTime.ofInstant(initialDate.toInstant(),
        ZoneId.systemDefault());

    System.out.println("default zone " + ZoneId.systemDefault());

    Object[] paramsOdt = { odt, odt, odt.toString(), "offset date time default zone" };

    jdbcTemplate.update(insertTimestampQuery, paramsOdt);

    //Offset date time Z - UTC

    System.out.println("zone 7 " + ZoneId.of("Z"));

    OffsetDateTime odt7 = OffsetDateTime.ofInstant(initialDate.toInstant(),
        ZoneId.of("Z"));

    Object[] params7Odt = { odt7, odt7, odt7.toString(), "offset date time zone Z" };

    jdbcTemplate.update(insertTimestampQuery, params7Odt);

    //Offset date time Z - UTC+8:00

    ZoneId zone = ZoneId.of("Asia/Vladivostok");
    ZoneOffset zoneOffSet = zone.getRules().getOffset(ldt);

    OffsetDateTime odtVLD = OffsetDateTime.ofInstant(initialDate.toInstant(),
        zoneOffSet);

    Object[] paramsVLDOdt = { odtVLD, odtVLD, odtVLD.toString(), "offset date time zone VLAT(Vladivostok)" };

    jdbcTemplate.update(insertTimestampQuery, paramsVLDOdt);

  /*  //Zoned datetime. Can't infer the SQL type to use for an instance of java.time.ZonedDateTime.
    ZonedDateTime zdt = ZonedDateTime.ofInstant(initialDate.toInstant(),
        ZoneId.systemDefault());

    Object[] paramsZdt = { zdt, zdt, zdt.toString(), "zoned date time default zone" };

    jdbcTemplate.update(c -> {
      final PreparedStatement statement = c.prepareStatement(SQL_INSERT_ADDRESS, new String[]{"id"});
      statement.setObject(1, now);
      statement.setObject(2, now);
      statement.setInt(3, version);
      statement.setString(4, address1);
      statement.setString(5, address2);
      statement.setString(6, city);
      statement.setString(7, country);
      statement.setString(8, region);
      statement.setString(9, zip);
      return statement;

    }insertTimestampQuery, paramsZdt);*/
    // Date as string. Need to cast ti  Postgres does not accept YYYY-MM-DD
    String insertWithTimestampQuery = "INSERT INTO test_datatime_table (ts_naked, ts_tz, ts_str, comment) "
        + "VALUES (CAST(TO_TIMESTAMP(?,'YYYY-MM-DD') AS TIMESTAMP), "
        + "(CAST(TO_TIMESTAMP(?,'YYYY-MM-DD') AS TIMESTAMP) AT TIME ZONE 'UTC'),?,?)";

    String dateStr = "2019-11-21";

    Object[] paramsDateStr = { dateStr, dateStr, dateStr, "date as string YYYY-MM-DD" };

    jdbcTemplate.update(insertWithTimestampQuery, paramsDateStr);


    /*// Date time as string
    String dateTimeStr = "2019-11-21T22:20:22.1234Z";

    Object[] paramsDateTimeStr = { dateTimeStr, dateTimeStr, dateTimeStr, "date-time as string YYYY-MM-DDTHH:MM:SS.ssssZ" };
    jdbcTemplate.update(insertTimestampQuery, paramsDateStr);
*/

  }

  @DeleteMapping
  @Transactional
  public void eraseDates() {
    String deleteQSL = "DELETE FROM test_datatime_table";
    jdbcTemplate.execute(deleteQSL);
  }

  @PostMapping("/external")
  @Transactional
  public void createRecord(@RequestBody DateTimeRecordDto dto) {
    String insertTimestampQuery = "INSERT INTO test_datatime_table (ts_naked, ts_tz, ts_str, comment) VALUES (?,?,?,?)";

    Object[] paramsLd = { dto.getLd(), dto.getLd(), dto.getLd().toString(), "local date. External" };

    jdbcTemplate.update(insertTimestampQuery, paramsLd);

    Object[] paramsLdt = { dto.getLdt(), dto.getLdt(), dto.getLdt().toString(), "local datetime. External" };

    jdbcTemplate.update(insertTimestampQuery, paramsLdt);

    Object[] paramsOdt = { dto.getOdt(), dto.getOdt(), dto.getOdt().toString(), "offset datetime. External" };

    jdbcTemplate.update(insertTimestampQuery, paramsOdt);

    Object[] paramsOdtz = { dto.getOdtz(), dto.getOdtz(), dto.getOdtz().toString(), "offset datetime Z. External" };

    jdbcTemplate.update(insertTimestampQuery, paramsOdtz);
  }
}
