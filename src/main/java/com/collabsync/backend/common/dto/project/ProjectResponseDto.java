package com.collabsync.backend.common.dto.project;

import com.collabsync.backend.domain.model.User;
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
    private User user;
    private String createdAt;
    private String updatedAt;
}
