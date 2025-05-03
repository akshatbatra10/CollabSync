package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.task.TaskRequestDto;
import com.collabsync.backend.common.dto.task.TaskResponseDto;
import com.collabsync.backend.common.enums.TaskStatus;
import com.collabsync.backend.domain.model.Project;
import com.collabsync.backend.domain.model.Task;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.TaskRepository;
import com.collabsync.backend.service.TaskService;
import com.collabsync.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public TaskResponseDto createTask(TaskRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .project(Project.builder().id(request.getProjectId()).build())
                .createdBy(username)
                .build();

        Task saved = taskRepository.save(task);
        return mapToDto(saved);
    }

    @Override
    public List<TaskResponseDto> getTasksByProjectId(Integer projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private TaskResponseDto mapToDto(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdBy(task.getCreatedBy())
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .createdAt(task.getCreatedAt().format(formatter))
                .updatedAt(task.getUpdatedAt() != null ? task.getUpdatedAt().format(formatter) : null)
                .build();
    }
}
