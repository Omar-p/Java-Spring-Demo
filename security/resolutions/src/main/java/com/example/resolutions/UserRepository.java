package com.example.resolutions;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository
    extends CrudRepository<User, UUID> {
}
