package com.example.springoauth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FacebookUser implements OAuth2User {

  private String name, email, id;

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.EMPTY_MAP;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList("ROLE_USER");
  }

  @Override
  public String getName() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFullName() {
    return name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return name.split(" ")[0];
  }

  public String getLastName() {
    return name.split(" ")[1];
  }

  public String getEmail() {
    return email;
  }

  public String getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return this.id;
  }
}

