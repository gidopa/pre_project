package com.greencat.pre_project.application.dto.user;

import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.domain.enums.UserRole;
import java.util.UUID;

public record UserResponseDto (UUID userId,
                               String userName,
                               String password,
                               String email,
                               UserRole userRole){
  public static UserResponseDto fromUser(Users user) {
    return new UserResponseDto(
      user.getUserId(),
      user.getUsername(),
      user.getPassword(),
      user.getEmail(),
      user.getUserRole()
    );
  }
}

