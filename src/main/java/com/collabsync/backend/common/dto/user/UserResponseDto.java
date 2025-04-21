package com.collabsync.backend.common.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private int id;
    private String username;
    private String email;
    private String fullName;
}
