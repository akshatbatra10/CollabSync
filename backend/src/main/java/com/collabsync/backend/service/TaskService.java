package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.task.TaskRequestDto;
import com.collabsync.backend.common.dto.task.TaskResponseDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(TaskRequestDto task);

    List<TaskResponseDto> getTasksByProjectId(Integer projectId);

    void assignTask(Integer taskId, String username);

    void changeTaskStatus(Integer taskId, String status);

    TaskResponseDto updateTask(Integer taskId, TaskRequestDto task);

    void changeTaskPriority(Integer taskId, String priority);

    void deleteTask(Integer taskId);
}
