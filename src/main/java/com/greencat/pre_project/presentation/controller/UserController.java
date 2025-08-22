package com.greencat.pre_project.presentation.controller;

import com.greencat.pre_project.application.dto.user.UserCreateRequest;
import com.greencat.pre_project.application.dto.user.UserResponseDto;
import com.greencat.pre_project.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "User 생성", description = "username, password, email 을 받아 유저를 생성")
  @PostMapping
  public UserResponseDto createUser(@RequestBody UserCreateRequest request) {
    return userService.creatUser(request);
  }
}
