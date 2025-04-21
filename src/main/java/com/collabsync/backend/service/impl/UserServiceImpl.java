package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.dto.user.UserSignupRequestDto;
import com.collabsync.backend.common.exceptions.DuplicateUserException;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.UserRepository;
import com.collabsync.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserSignupRequestDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email already in use");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserException("Username already in use");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getFullName());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
