package com.example.springoauth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioController {


  @GetMapping("/portfolio")

  public String portfolio() {
    return "portfolio";
  }

  @GetMapping("/login")

  public String login() {
    return "login";
  }
}
