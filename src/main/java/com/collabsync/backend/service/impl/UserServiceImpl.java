package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.user.UserLoginRequestDto;
import com.collabsync.backend.common.dto.user.UserLoginResponseDto;
import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.dto.user.UserSignupRequestDto;
import com.collabsync.backend.common.enums.Role;
import com.collabsync.backend.common.exceptions.DuplicateUserException;
import com.collabsync.backend.common.exceptions.InvalidCredentialsException;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.UserRepository;
import com.collabsync.backend.security.JwtService;
import com.collabsync.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
                .role(Role.ROLE_USER)
                .build();

        log.info("Creating user: {}", user);

        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getFullName());
    }

    @Override
    public UserLoginResponseDto loginUser(UserLoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        boolean isPasswordValid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isPasswordValid) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new UserLoginResponseDto("Login successful", token);
    }

    @Override
    public User getCurrentlyLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
