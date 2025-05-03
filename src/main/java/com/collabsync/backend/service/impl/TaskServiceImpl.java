package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.task.TaskRequestDto;
import com.collabsync.backend.common.dto.task.TaskResponseDto;
import com.collabsync.backend.common.enums.TaskPriority;
import com.collabsync.backend.common.enums.TaskStatus;
import com.collabsync.backend.common.exceptions.ResourceNotFoundException;
import com.collabsync.backend.domain.model.Project;
import com.collabsync.backend.domain.model.Task;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.ProjectRepository;
import com.collabsync.backend.repository.TaskRepository;
import com.collabsync.backend.service.TaskService;
import com.collabsync.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public TaskResponseDto createTask(TaskRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + request.getProjectId()));

        Task.TaskBuilder taskBuilder = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.LOW)
                .project(project)
                .createdBy(username);

        if (request.getAssignedTo() != null && !request.getAssignedTo().trim().isBlank()) {
            User assignedUser = userService.findByUsername(request.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            boolean isCollaborator = project.getMembers().stream()
                    .anyMatch(member -> member.getId().equals(assignedUser.getId()));

            if (!isCollaborator) {
                throw new ResourceNotFoundException("User is not a collaborator");
            }

            taskBuilder.assignedTo(assignedUser);
        }

        Task saved = taskRepository.save(taskBuilder.build());
        return mapToDto(saved);
    }

    @Override
    public List<TaskResponseDto> getTasksByProjectId(Integer projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void assignTask(Integer taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        User assignedUser = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Assigning task {} to user {}", taskId, username);
        boolean isCollaborator = task.getProject().getMembers().stream()
                .anyMatch(member -> member.getId().equals(assignedUser.getId()));

        if (!isCollaborator) {
            throw new ResourceNotFoundException("User is not a collaborator");
        }

        task.setAssignedTo(assignedUser);
        taskRepository.save(task);
    }

    private TaskResponseDto mapToDto(Task task) {

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority().name())
                .assignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null)
                .createdBy(task.getCreatedBy())
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .createdAt(task.getCreatedAt().format(formatter))
                .updatedAt(task.getUpdatedAt() != null ? task.getUpdatedAt().format(formatter) : null)
                .build();
    }
}
