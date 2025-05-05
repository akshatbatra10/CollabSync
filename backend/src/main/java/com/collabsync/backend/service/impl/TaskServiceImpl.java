package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.task.TaskRequestDto;
import com.collabsync.backend.common.dto.task.TaskResponseDto;
import com.collabsync.backend.common.enums.EventType;
import com.collabsync.backend.common.enums.ProjectRole;
import com.collabsync.backend.common.enums.TaskPriority;
import com.collabsync.backend.common.enums.TaskStatus;
import com.collabsync.backend.common.exceptions.InvalidCredentialsException;
import com.collabsync.backend.common.exceptions.ResourceNotFoundException;
import com.collabsync.backend.domain.model.Project;
import com.collabsync.backend.domain.model.Task;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.kafka.model.TaskChangeEvent;
import com.collabsync.backend.kafka.model.TaskDeleteEvent;
import com.collabsync.backend.kafka.producer.EventPublisher;
import com.collabsync.backend.repository.ProjectRepository;
import com.collabsync.backend.repository.TaskRepository;
import com.collabsync.backend.service.TaskService;
import com.collabsync.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final EventPublisher eventPublisher;

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

            validateCollaborator(project, assignedUser);

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
    @Transactional
    public void assignTask(Integer taskId, String username) {
        String currentlyLoggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        User assignedUser = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Assigning task {} to user {}", taskId, username);
        validateCollaborator(task.getProject(), assignedUser);

        task.setAssignedTo(assignedUser);
        taskRepository.save(task);

        TaskChangeEvent taskChangeEvent = TaskChangeEvent.builder()
                .taskId(taskId)
                .recipientId(task.getAssignedTo().getId())
                .updatedBy(currentlyLoggedInUser)
                .build();

        BaseEvent<TaskChangeEvent> baseEvent = BaseEvent.<TaskChangeEvent>builder()
                .eventType(EventType.TASK_ASSIGNED)
                .timestamp(LocalDateTime.now())
                .payload(taskChangeEvent)
                .build();

        eventPublisher.publish("task-events", baseEvent);
    }

    @Override
    @Transactional
    public void changeTaskStatus(Integer taskId, String status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        TaskStatus newStatus = getEnum(status, TaskStatus.class);

        Project project = task.getProject();
        authorizeProjectModification(project, username);

        task.setStatus(newStatus);
        createTaskEvent(taskId, username, task);
    }

    @Override
    @Transactional
    public void changeTaskPriority(Integer taskId, String priority) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        TaskPriority newPriority = getEnum(priority, TaskPriority.class);

        Project project = task.getProject();
        authorizeProjectModification(project, username);

        task.setPriority(newPriority);
        createTaskEvent(taskId, username, task);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Integer taskId, TaskRequestDto request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Project project = task.getProject();
        authorizeProjectModification(project, username);

        task.setTitle(request.getTitle() != null ? request.getTitle() : task.getTitle());
        task.setDescription(request.getDescription() != null ? request.getDescription() : task.getDescription());
        createTaskEvent(taskId, username, task);

        return mapToDto(task);
    }

    public void deleteTask(Integer taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Project project = task.getProject();
        authorizeProjectModification(project, username);

        taskRepository.deleteById(taskId);

        TaskDeleteEvent taskDeleteEvent = TaskDeleteEvent.builder()
                .taskId(taskId)
                .recipientId(task.getAssignedTo().getId())
                .build();

        BaseEvent<TaskDeleteEvent> baseEvent = BaseEvent.<TaskDeleteEvent>builder()
                .eventType(EventType.TASK_DELETED)
                .timestamp(LocalDateTime.now())
                .build();

        eventPublisher.publish("task-events", baseEvent);
    }

    private void createTaskEvent(Integer taskId, String username, Task task) {
        taskRepository.save(task);

        TaskChangeEvent taskChangeEvent = TaskChangeEvent.builder()
                .taskId(taskId)
                .recipientId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .updatedBy(username)
                .build();

        BaseEvent<TaskChangeEvent> baseEvent = BaseEvent.<TaskChangeEvent>builder()
                .eventType(EventType.TASK_UPDATED)
                .timestamp(LocalDateTime.now())
                .payload(taskChangeEvent)
                .build();

        eventPublisher.publish("task-events", baseEvent);
    }

    private void authorizeProjectModification(Project project, String username) {
        if (project.getMembers().stream()
                .noneMatch(member -> member.getUser().getUsername().equals(username) && member.getRole() != ProjectRole.VIEWER)) {
            throw new InvalidCredentialsException("User is not authorized to modify the task");
        }
    }

    private <T extends Enum<T>> T getEnum(String value, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCredentialsException("Invalid value");
        }
    }

    private void validateCollaborator(Project project, User user) {
        boolean isCollaborator = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(user.getId()) && member.getRole() != ProjectRole.VIEWER);

        if (!isCollaborator) {
            throw new ResourceNotFoundException("User is not a collaborator");
        }
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
