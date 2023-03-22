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

  @OneToMany(cascade = CascadeType.ALL)
  @Builder.Default
  List<UserAuthority> authorities = new ArrayList<>();

  public void addAuthority(String authority) {
    this.authorities.add(new UserAuthority(this, authority));
  }
}
