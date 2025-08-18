package com.greencat.pre_project.infrastructure.repository;

import com.greencat.pre_project.domain.entity.Users;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, UUID> {

  boolean existsByUsername(String username);
  Optional<Users> findByUsername(String username);
}
