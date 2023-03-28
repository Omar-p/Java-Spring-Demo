package com.example.blog.model;

import java.util.List;

public record PostResponse(
    List<PostDto> content,
    int pageNo,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean last
) {

}
