package com.collabsync.backend.common.dto.project;

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
    private String createdBy;
    private String createdAt;
    private String updatedAt;
}
