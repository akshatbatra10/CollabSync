package com.collabsync.backend.common.dto.project;

import com.collabsync.backend.common.annotations.EnumValidator;
import com.collabsync.backend.common.enums.ProjectRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaboratorResponseDto {

    private Integer id;
    private String username;
    private String email;
    private String fullName;
    @EnumValidator(enumClass = ProjectRole.class)
    private ProjectRole role;
}
