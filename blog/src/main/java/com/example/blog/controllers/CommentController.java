package com.example.blog.controllers;


import com.example.blog.model.CommentDto;
import com.example.blog.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{postId}/comments")
  public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
    Long commentId = commentService.createComment(postId, commentDto);
    return ResponseEntity.created(URI.create("/api/posts/" + postId + "/comments/" + commentId)).build();
  }

  @GetMapping("/{postId}/comments")
  public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
    return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
  }

  @GetMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<?> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
    return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
  }

  @PutMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentUpdate) {
    var updatedComment = commentService.updateComment(postId, commentId, commentUpdate);
    return ResponseEntity.ok(updatedComment);
  }

  @DeleteMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
    commentService.deleteComment(postId, commentId);
    return ResponseEntity.noContent().build();
  }
}
