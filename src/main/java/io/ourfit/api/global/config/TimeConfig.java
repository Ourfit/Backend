package io.ourfit.api.global.config;

import java.time.Clock;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig implements InitializingBean {

  /** 애플리케이션의 기본 시간대 */
  public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

  @Override
  public void afterPropertiesSet() {
    TimeZone.setDefault(TimeZone.getTimeZone(DEFAULT_ZONE_ID));
  }

  @Bean
  public Clock clock() {
    return Clock.system(DEFAULT_ZONE_ID);
  }
}
