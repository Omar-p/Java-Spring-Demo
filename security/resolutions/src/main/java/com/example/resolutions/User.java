package com.example.resolutions;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(unique = true)
  String username;

  String password;

  Boolean enabled = true;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @Builder.Default
  List<UserAuthority> userAuthorities = new ArrayList<>();

  public User(User user) {
    this.id = user.id;
    this.username = user.username;
    this.password = user.password;
    this.enabled = user.enabled;
    this.userAuthorities = new ArrayList<>(user.userAuthorities);
  }
  public void addAuthority(String authority) {
    this.userAuthorities.add(new UserAuthority(this, authority));
  }
}
