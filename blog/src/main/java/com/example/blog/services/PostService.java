package com.example.blog.services;

import com.example.blog.model.PostDto;
import com.example.blog.model.PostResponse;

public interface PostService {
  Long createPost(PostDto postDto);

  PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortOrder);

  PostDto getPostById(Long id);

  PostDto updatePost(PostDto postDto);

  void deletePost(Long id);
}
