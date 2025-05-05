package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.user.UserLoginRequestDto;
import com.collabsync.backend.common.dto.user.UserLoginResponseDto;
import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.dto.user.UserSignupRequestDto;
import com.collabsync.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid UserSignupRequestDto request) {
        UserResponseDto userResponseDto = userService.createUser(request);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto request) {
        UserLoginResponseDto userLoginResponseDto = userService.loginUser(request);
        return ResponseEntity.ok(userLoginResponseDto);
    }

    @GetMapping("/me")
    public String currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();

        return "User: " + username + ", IP: " + details.getRemoteAddress();
    }

}
