package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.user.UserLoginRequestDto;
import com.collabsync.backend.common.dto.user.UserLoginResponseDto;
import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.dto.user.UserSignupRequestDto;
import com.collabsync.backend.common.exceptions.DuplicateUserException;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.UserRepository;
import com.collabsync.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldCreateUserSuccessfully() {

        UserSignupRequestDto requestDto = new UserSignupRequestDto("John", "john@gmail.com", "John Doe", "john123");

        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1)
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password("encodedPassword")
                .build();

        when(userRepository.save(savedUser)).thenReturn(savedUser);

        UserResponseDto user = userService.createUser(requestDto);

        assertEquals("John", user.getUsername());
        assertEquals("john@gmail.com", user.getEmail());

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserSignupRequestDto request = new UserSignupRequestDto("John", "john@gmail.com", "John Doe", "john123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.createUser(request));
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = User.builder()
                .email("john@gmail.com")
                .password("encodedPassword")
                .build();

        UserLoginRequestDto request = new UserLoginRequestDto("john@gmail.com", "password");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");

        UserLoginResponseDto response = userService.loginUser(request);

        assertEquals("Login successful", response.getMessage());
        assertEquals("token", response.getToken());
    }
}
