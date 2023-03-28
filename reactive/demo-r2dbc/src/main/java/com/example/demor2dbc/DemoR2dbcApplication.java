package com.example.demor2dbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@SpringBootApplication
@EnableR2dbcAuditing
public class DemoR2dbcApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoR2dbcApplication.class, args);
  }

}
