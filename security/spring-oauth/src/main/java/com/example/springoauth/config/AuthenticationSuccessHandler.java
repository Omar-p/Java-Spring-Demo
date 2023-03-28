package com.example.springoauth.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

  private final UserService userService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    if (authentication instanceof OAuth2AuthenticationToken oAuth2Token) {
      var username = oAuth2Token.getPrincipal().getName();
      if (UserService.findByUsername().isPresent()) {
        return;
      }
      String email = "", firstName = "", lastName = "";
      if (oAuth2Token.getAuthorizedClientRegistrationId().equals("facebook")) {
        OAuth2User user = oAuth2Token.getPrincipal();
        email = user.getAttribute("email");
        String fullname = user.getAttribute("name");
        firstName = fullname.split(" ")[0];
        lastName = fullname.split(" ")[1];
      } else if (oAuth2Token.getPrincipal() instanceof OidcUser oidcUser) {
        email = oidcUser.getEmail();
        firstName = oidcUser.getGivenName();
        lastName = oidcUser.getFamilyName();
      }

      RegistratioNRequest registratioNRequest = new RegistratioNRequest();
      registratioNRequest.setEmail(email);
      registratioNRequest.setFirstName(firstName);
      registratioNRequest.setLastName(lastName);
      userService.registerNewUser(registratioNRequest);
    }
  }
}
