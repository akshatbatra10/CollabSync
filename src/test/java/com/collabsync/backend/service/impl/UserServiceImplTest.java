package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.dto.user.UserSignupRequestDto;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.UserRepository;
import com.collabsync.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                .username("John")
                .email("john@gmail.com")
                .password("encodedPassword")
                .build();

        when(userRepository.save(savedUser)).thenReturn(savedUser);

        UserResponseDto user = userService.createUser(requestDto);

        assertEquals("John", user.getUsername());
        assertEquals("john@gmail.com", user.getEmail());

    }
}
