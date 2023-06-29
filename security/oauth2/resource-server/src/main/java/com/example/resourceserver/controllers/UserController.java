package com.example.resourceserver.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/status/check")
  public String status() {
    return "Working...";
  }

  @GetMapping("/token")
  public Map<String, Jwt> getToken(@AuthenticationPrincipal Jwt jwt) {
    return Map.of("principle", jwt);
  }

  @GetMapping
  public String getUser(Authentication authentication) {
    System.out.println(authentication.getAuthorities());
    return "getUser";
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("#jwt.subject == #id")
  String deleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable String id) {
    return "Deleted user " + jwt.getSubject();
  }
}
