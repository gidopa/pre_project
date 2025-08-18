package com.greencat.pre_project.application.mapper;

import com.greencat.pre_project.application.dto.user.UserCreateRequest;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.domain.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public Users createRequestToEntity(UserCreateRequest request) {
    return Users.builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .email(request.getEmail())
        .userRole(UserRole.USER)
        .build();
  }

}
