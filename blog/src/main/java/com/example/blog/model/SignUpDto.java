package com.example.blog.model;

public record SignUpDto(
  String name,
  String username,
  String email,
  String password
) {
}
