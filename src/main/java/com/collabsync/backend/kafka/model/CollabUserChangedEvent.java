package com.collabsync.backend.kafka.model;

import com.collabsync.backend.common.enums.ProjectRole;
import com.collabsync.backend.kafka.enums.ChangeType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollabUserChangedEvent {
    private Integer projectId;
    private Integer userId;
    private ChangeType changeType;
    private ProjectRole role;
    private String createdBy;
    private Integer recipientId;
}