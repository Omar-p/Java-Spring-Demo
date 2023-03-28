package com.example.resolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class ResolutionsApplication {


  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(
            authz -> authz
                .requestMatchers(HttpMethod.GET, "/resolutions/**").hasAuthority("READ")
                .anyRequest().hasAuthority("WRITE")
        )
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }


  public static void main(String[] args) {
    SpringApplication.run(ResolutionsApplication.class, args);
  }

}
