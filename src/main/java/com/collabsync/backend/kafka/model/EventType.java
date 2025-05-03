package com.collabsync.backend.kafka.model;

public enum EventType {
    TASK_CREATED,
    TASK_UPDATED,
    TASK_DELETED,
    PROJECT_CREATED,
    PROJECT_UPDATED,
    PROJECT_DELETED,
    COMMENT_CREATED,
    COMMENT_UPDATED,
    COMMENT_DELETED,
    TASK_ASSIGNED,
    USER_MENTIONED,
}
