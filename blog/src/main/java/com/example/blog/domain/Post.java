package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "title"
    })
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String content;

  @Builder.Default
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post", fetch = FetchType.LAZY)
  private Set<Comment> comments = new HashSet<>();

  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setPost(this);
  }
}

