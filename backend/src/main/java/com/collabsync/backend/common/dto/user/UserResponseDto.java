package com.collabsync.backend.common.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
}
