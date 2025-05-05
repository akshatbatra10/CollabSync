package com.collabsync.backend.common.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private int status;
    private String error;
    private String path;
}
