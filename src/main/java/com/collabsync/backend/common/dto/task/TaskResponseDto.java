package com.collabsync.backend.common.dto.task;

import com.collabsync.backend.common.enums.TaskStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {

    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private Integer projectId;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
}
