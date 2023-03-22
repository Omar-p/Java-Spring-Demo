package com.example.resolutions;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(BridgeUser::new)
        .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
  }


  private static class BridgeUser  extends User implements UserDetails {
    public BridgeUser(User user) {
      super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return super.getUserAuthorities()
          .stream()
          .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority()))
          .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
      return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
      return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return enabled;
    }

    @Override
    public boolean isEnabled() {
      return enabled;
    }
  }
}
