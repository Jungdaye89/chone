package com.chone.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChoneApplication {

  public static void main(String[] args) {
    SpringApplication.run(ChoneApplication.class, args);
  }
}