package com.example.resolutions;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "Resolution")
@Getter
@Setter
@NoArgsConstructor
public class Resolution {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column
  private String text;

  @Column
  private UUID owner;

  @Column(nullable=false)
  private Boolean completed = false;


  public Resolution(String text, UUID owner) {
    this.text = text;
    this.owner = owner;
  }

}
