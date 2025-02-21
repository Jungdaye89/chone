package com.chone.server.commons.config;

import com.chone.server.commons.lock.DistributedLockTemplate;
import com.chone.server.commons.lock.InMemoryLockTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class LockConfig {
  @Bean
  public DistributedLockTemplate distributedLockTemplate() {
    return new InMemoryLockTemplate();
  }
}
