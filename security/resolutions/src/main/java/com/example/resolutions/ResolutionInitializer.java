package com.example.resolutions;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResolutionInitializer implements SmartInitializingSingleton {
  private final ResolutionRepository resolutions;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void afterSingletonsInstantiated() {
    UUID joshId = UUID.fromString("219168d2-1da4-4f8a-85d8-95b4377af3c1");
    UUID carolId = UUID.fromString("328167d1-2da3-5f7a-86d7-96b4376af2c0");

    this.resolutions.save(new Resolution("Read War and Peace", joshId));
    this.resolutions.save(new Resolution("Free Solo the Eiffel Tower", joshId));
    this.resolutions.save(new Resolution("Hang Christmas Lights", joshId));

    this.resolutions.save(new Resolution("Run for President", carolId));
    this.resolutions.save(new Resolution("Run a Marathon", carolId));
    this.resolutions.save(new Resolution("Run an Errand", carolId));

    final User omar = User.builder().username("omar").password(passwordEncoder.encode("omar")).enabled(true).build();
    omar.addAuthority("READ");
    final User josh = User.builder().username("josh").password(passwordEncoder.encode("josh")).enabled(true).build();
    josh.addAuthority("READ");
    josh.addAuthority("WRITE");
    userRepository.saveAll(List.of(omar, josh));
  }
}