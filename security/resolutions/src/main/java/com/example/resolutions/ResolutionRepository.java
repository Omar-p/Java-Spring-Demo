package com.example.resolutions;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ResolutionRepository
    extends CrudRepository<Resolution, UUID> {
  List<Resolution> findByOwner(UUID owner);

  @Modifying
  @Query("UPDATE Resolution SET text = :text WHERE id = :id")
  void revise(@Param("id") UUID id, @Param("text") String text);

  @Modifying
  @Query("UPDATE Resolution SET completed = true WHERE id = :id")
  void complete(@Param("id") UUID id);
}