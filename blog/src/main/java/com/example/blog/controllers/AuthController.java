package com.example.blog.controllers;

import com.example.blog.model.LoginDto;
import com.example.blog.model.SignUpDto;
import com.example.blog.repositories.RoleRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto, @CurrentSecurityContext SecurityContext securityContext) {
    var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.usernameOrEmail(), loginDto.password()));

    return ResponseEntity.ok(authService.generateToken(authentication));
  }

  // validate username and email are unique and persist user
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

    if (userRepository.existsByUsername(signUpDto.username())) {
      return ResponseEntity.badRequest().body("Error: Username is already taken!");
    }

    if (userRepository.existsByEmail(signUpDto.email())) {
      return ResponseEntity.badRequest().body("Error: Email is already in use!");
    }

    // Create new user's account
    var user = new com.example.blog.domain.User(signUpDto.name(), signUpDto.username(), signUpDto.email(), signUpDto.password());
    user.addRole(roleRepository.findByName("ROLE_USER").orElseThrow());
    userRepository.save(user);
    return ResponseEntity.ok("User registered successfully!");
  }
}
