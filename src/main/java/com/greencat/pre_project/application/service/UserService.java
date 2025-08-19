package com.greencat.pre_project.application.service;

import com.greencat.pre_project.application.dto.user.UserCreateRequest;
import com.greencat.pre_project.application.dto.user.UserResponseDto;
import com.greencat.pre_project.domain.entity.Users;
import com.greencat.pre_project.exception.error_code.UserErrorCode;
import com.greencat.pre_project.exception.exception.PreTaskException;
import com.greencat.pre_project.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponseDto creatUser(UserCreateRequest request) {
    Users user = Users.createRequestToEntity(request);
    checkUsernameExists(user.getUsername());
    try {
      Users saved = userRepository.save(user);
      return UserResponseDto.changeEntityToResponse(saved);
    } catch (DataIntegrityViolationException e) {
      // DB 유니크 제약과 충돌 → 동시 요청 등
      throw new PreTaskException(UserErrorCode.DUPLICATE_USER);
    }
  }

  public void checkUsernameExists(String username) {
    if(userRepository.existsByUsername(username)) {
      throw new PreTaskException(UserErrorCode.DUPLICATE_USER);
    }

  }

  public Users findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new PreTaskException(UserErrorCode.NOT_FOUND));
  }
}
