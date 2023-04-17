package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(value = {"id"}, allowGetters = true)
public record CommentDto(
    Long id,

    @NotEmpty(message = "Name shouldn't be null or empty") String name,
    @NotEmpty @Email String email,
    @NotEmpty @Size(min = 10) String body

) {
}
