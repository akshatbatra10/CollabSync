package com.collabsync.backend.common.dto.project;

import com.collabsync.backend.common.dto.user.UserResponseDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectResponseDto {

    private int id;
    private String name;
    private String description;
    private UserResponseDto user;
    private String createdAt;
    private String updatedAt;
}
