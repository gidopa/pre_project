package com.greencat.pre_project.domain.entity;

import com.greencat.pre_project.application.dto.user.UserCreateRequest;
import com.greencat.pre_project.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLRestriction("is_deleted is false")
public class Users extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(unique = true, name = "user_id")
  private UUID userId;

  @NotBlank
  @Column(nullable = false, length = 15, unique = true)
  private String username;
  private String password;
  @Email
  private String email;
  @Enumerated(EnumType.STRING)
  @Column(name = "user_role")
  private UserRole userRole;

  @Column(name = "todo_list")
  @OneToMany(mappedBy = "user")
  private List<Todo> todoList = new ArrayList<>();

  public static Users createRequestToEntity(UserCreateRequest request) {
    return Users.builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .email(request.getEmail())
        .userRole(UserRole.USER)
        .build();
  }
}
