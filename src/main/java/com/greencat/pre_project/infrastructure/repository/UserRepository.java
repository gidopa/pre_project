package com.greencat.pre_project.infrastructure.repository;

import com.greencat.pre_project.domain.entity.Users;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, UUID> {

  boolean existsByUsername(String username);

  @Query("select u from Users u where lower(u.username) = lower(:username)")
  Optional<Users> findByUsername(String username);
}
