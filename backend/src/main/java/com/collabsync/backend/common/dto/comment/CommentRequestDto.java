package com.collabsync.backend.common.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

    @NotBlank(message = "Comment content is required")
    private String content;

    @NotNull(message = "Task ID is required")
    private Integer taskId;
}
