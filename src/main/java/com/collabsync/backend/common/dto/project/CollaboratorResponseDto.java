package com.collabsync.backend.common.dto.project;

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
    private String role;
}
