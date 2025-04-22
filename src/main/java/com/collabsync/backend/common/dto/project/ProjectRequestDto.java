package com.collabsync.backend.common.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequestDto {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;
}
