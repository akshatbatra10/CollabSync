package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.dto.user.UserSignupRequestDto;
import com.collabsync.backend.domain.model.User;

import java.util.Optional;

public interface UserService {

    UserResponseDto createUser(UserSignupRequestDto request);

    Optional<User> findByEmail(String email);
}
