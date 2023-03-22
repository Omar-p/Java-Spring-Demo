package com.example.resolutions;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "authorities")
public class UserAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @ManyToOne
  @JoinColumn(name = "username", referencedColumnName = "username")
  User user;

  String authority;

  public UserAuthority(User user, String authority) {
    this.user = user;
    this.authority = authority;
  }
}
