package com.collabsync.backend.common.dto.task;

import com.collabsync.backend.common.enums.TaskPriority;
import com.collabsync.backend.common.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDto {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    private TaskStatus status;
    private TaskPriority priority;
    private String assignedTo;
}
