package com.collabsync.backend.kafka.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateEvent {
    private Integer projectId;
    private String updatedBy;
    private Integer recipientId;
}
