package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

@JsonIgnoreProperties(value = {"id", "comments"}, allowGetters = true)

public record PostDto(
    Long id,
    @NotEmpty @Size(min = 2) String title,
    @NotEmpty @Size(min = 10) String description,
    @NotEmpty String content,
    Set<CommentDto> comments) {
  public PostDto(Long id, String title, String description, String content) {
    this(id, title, description, content, Set.of());
  }
}
