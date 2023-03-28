package com.example.springoauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  private final AuthenticationSuccessHandler authenticationSuccessHandler;

  public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
    this.authenticationSuccessHandler = authenticationSuccessHandler;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
          .requestMatchers("/login", "/register", "/css/**", "/webjars/**").permitAll()
          .anyRequest().authenticated()
      ).formLogin((formLogin) -> formLogin
          .loginPage("/login")
      ).oauth2Login((oauth2Login) -> oauth2Login
          .loginPage("/login")
            .successHandler(this.authenticationSuccessHandler)
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService())
        )
        .logout()
        .and()
        .build();

  }


}
