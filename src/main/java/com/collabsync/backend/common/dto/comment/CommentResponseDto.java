package com.collabsync.backend.common.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Integer id;
    private String content;
    private String createdBy;
    private Integer taskId;
    private String createdAt;
    private String updatedAt;
}
