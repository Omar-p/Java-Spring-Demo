package com.example.blog.services;

import com.example.blog.domain.Post;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.model.PostDto;
import com.example.blog.model.PostResponse;
import com.example.blog.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SimplePostService implements PostService {

  private final PostRepository postRepository;

  @Override
  public Long createPost(PostDto postDto) {
    return postRepository.save(toPost(postDto)).getId();
  }

  @Override
  public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortOrder) {
    var pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder), sortBy));
    final Page<Post> page = postRepository.findAll(pageRequest);

    return postPageToPostResponse(page);
  }

  @Override
  public PostDto getPostById(Long id) {
    return toPostDto(postRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("post", "id", id.toString())
    ));
  }

  @Override
  @Transactional
  public PostDto updatePost(PostDto postDto) {
    var post = postRepository.findById(postDto.id())
        .orElseThrow(() -> new ResourceNotFoundException("post", "id", postDto.id().toString()));
    if (postDto.title() != null) {
      post.setTitle(postDto.title());
    }
    if (postDto.description() != null) {
      post.setDescription(postDto.description());
    }
    if (postDto.content() != null) {
      post.setContent(postDto.content());
    }

    return toPostDto(post);
  }

  @Override
  public void deletePost(Long id) {
    postRepository.deleteById(id);
  }

  private Post toPost(PostDto postDto) {
    return Post.builder()
        .title(postDto.title())
        .description(postDto.description())
        .content(postDto.content())
        .build();
  }

  private PostResponse postPageToPostResponse(Page<Post> page) {
    return new PostResponse(
        page.getContent().stream().map(this::toPostDto).toList(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast()
    );
  }

  private PostDto toPostDto(Post post) {
    return new PostDto(post.getId(), post.getTitle(), post.getDescription(), post.getContent());
  }
}
