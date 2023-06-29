package com.example.resourceserver.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    final Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
    final List<String> scope = Arrays.stream(source.getClaimAsString("scope").split(" ")).toList();


    var roles = (List<String>)realmAccess.get("roles");

    final List<GrantedAuthority> rolesGrantedAuthority = roles.stream()
        .map(roleName -> "ROLE_" + roleName)
        .map(SimpleGrantedAuthority::new)
        .collect(toList());

    final List<GrantedAuthority> scopeAuthorities = scope.stream()
        .map(scopeName -> "SCOPE_" + scopeName)
        .map(SimpleGrantedAuthority::new)
        .collect(toList());

    scopeAuthorities.addAll(rolesGrantedAuthority);

    return scopeAuthorities;
  }
}
