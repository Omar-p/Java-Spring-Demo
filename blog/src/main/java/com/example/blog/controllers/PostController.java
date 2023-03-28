package com.example.blog.controllers;

import com.example.blog.model.PostDto;
import com.example.blog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;
  @PostMapping
  public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
    Long postId = postService.createPost(postDto);
    return ResponseEntity.created(URI.create("/api/posts/" + postId)).build();
  }


  @GetMapping
  public ResponseEntity<?> getAllPosts(
      @RequestParam(value = "pageNo", defaultValue = "0") int pageNo ,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
    return ResponseEntity.ok(postService.getAllPosts(pageNo, pageSize, sortBy, sortOrder));
  }


  @GetMapping("/{id}")
  public ResponseEntity<?> getPostById(@PathVariable Long id) {
    return ResponseEntity.ok(postService.getPostById(id));
  }

  @PutMapping
  public ResponseEntity<?> updatePost(@RequestBody PostDto postDto) {
    var updatedPost = postService.updatePost(postDto);
    return ResponseEntity.ok(updatedPost);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePost(@PathVariable Long id) {
    postService.deletePost(id);
    return ResponseEntity.noContent().build();
  }
}
