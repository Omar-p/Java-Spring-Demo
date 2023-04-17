package com.example.blog.services;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.model.CommentDto;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  public Long createComment(Long postId, CommentDto commentDto) {
    var c = toComment(commentDto);
    final AtomicLong id = new AtomicLong();

    postRepository.findById(postId).ifPresentOrElse(post -> {
      c.setPost(post);
      id.set(commentRepository.save(c).getId());
    }, () -> {
      throw new ResourceNotFoundException("post", "id", postId.toString());
    });

    return id.get();
  }

  @Transactional(readOnly = true)
  public List<CommentDto> getCommentsByPostId(Long postId) {
    final Post post = postRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("post", "id", postId.toString()));
    return post.getComments().stream()
        .map(this::toCommentDto)
        .toList();
  }

  public CommentDto getCommentById(Long postId, Long commentId) {
    return commentRepository.getCommentByIdAndPostId(commentId, postId)
        .map(this::toCommentDto)
        .orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId.toString()));
  }


  @Transactional
  public CommentDto updateComment(Long postId, Long commentId, CommentDto commentUpdate) {
    final Comment comment = commentRepository.getCommentByIdAndPostId(commentId, postId)
        .orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId.toString()));

    comment.setName(Optional.ofNullable(commentUpdate.name()).orElse(comment.getName()));
    comment.setEmail(Optional.ofNullable(commentUpdate.email()).orElse(comment.getEmail()));
    comment.setBody(Optional.ofNullable(commentUpdate.body()).orElse(comment.getBody()));

    return toCommentDto(comment);
  }
  private Comment toComment(CommentDto commentDto) {
    return new Comment(
        commentDto.name(),
        commentDto.email(),
        commentDto.body()
    );
  }

  private CommentDto toCommentDto(Comment comment) {
    return new CommentDto(
        comment.getId(),
        comment.getName(),
        comment.getEmail(),
        comment.getBody()
    );
  }


  @Transactional
  public void deleteComment(Long postId, Long commentId) {
    final Comment comment = commentRepository.getCommentByIdAndPostId(commentId, postId)
        .orElseThrow(() -> new ResourceNotFoundException("comment", "id", commentId.toString()));
    commentRepository.delete(comment);
  }
}
