package com.collabsync.backend.kafka.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDeleteEvent {
    private Integer projectId;
    private Integer recipientId;
}
