package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.project.CollaboratorResponseDto;
import com.collabsync.backend.common.dto.project.ProjectRequestDto;
import com.collabsync.backend.common.dto.project.ProjectResponseDto;
import com.collabsync.backend.common.dto.user.UserResponseDto;
import com.collabsync.backend.common.enums.ProjectRole;
import com.collabsync.backend.common.exceptions.DuplicateUserException;
import com.collabsync.backend.common.exceptions.InvalidCredentialsException;
import com.collabsync.backend.common.exceptions.ResourceNotFoundException;
import com.collabsync.backend.domain.model.Project;
import com.collabsync.backend.domain.model.ProjectMember;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.kafka.enums.ChangeType;
import com.collabsync.backend.kafka.model.BaseEvent;
import com.collabsync.backend.kafka.enums.EventType;
import com.collabsync.backend.kafka.model.CollabUserChangedEvent;
import com.collabsync.backend.kafka.producer.EventPublisher;
import com.collabsync.backend.repository.ProjectRepository;
import com.collabsync.backend.service.ProjectService;
import com.collabsync.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final EventPublisher eventPublisher;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto request) {
        User currentUser = userService.getCurrentlyLoggedInUser();
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(currentUser)
                .build();

        Project savedProject = projectRepository.save(project);

        return mapToDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public void addOrRemoveCollaborator(Integer projectId, String username, String action) {
        ChangeType changeType;
        try {
            changeType = ChangeType.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCredentialsException("Invalid action");
        }

        if (changeType != ChangeType.ADD && changeType != ChangeType.REMOVE) {
            throw new InvalidCredentialsException("Invalid action");
        }

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        boolean alreadyExists = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(user.getId()));

        if (alreadyExists && changeType == ChangeType.ADD) {
            throw new DuplicateUserException("User already exists in project");
        }

        if (!alreadyExists && changeType == ChangeType.REMOVE) {
            throw new DuplicateUserException("User does not exists in project");
        }

        if (changeType == ChangeType.ADD) {
            ProjectMember projectMember = ProjectMember.builder()
                    .project(project)
                    .user(user)
                    .role(ProjectRole.CONTRIBUTOR)
                    .joinedAt(LocalDateTime.now())
                    .build();

            project.getMembers().add(projectMember);
            projectRepository.save(project);
        } else {
            project.getMembers().removeIf(member -> member.getId().equals(user.getId()));
            projectRepository.save(project);
        }

        CollabUserChangedEvent collabUserChangedEvent = CollabUserChangedEvent.builder()
                .userId(user.getId())
                .projectId(project.getId())
                .changeType(changeType)
                .role(ProjectRole.CONTRIBUTOR)
                .recipientId(user.getId())
                .build();

        BaseEvent<CollabUserChangedEvent> baseEvent = BaseEvent.<CollabUserChangedEvent>builder()
                .eventType(EventType.COLLAB_USER_CHANGED)
                .timestamp(LocalDateTime.now())
                .payload(collabUserChangedEvent)
                .build();

        eventPublisher.publish("project-events", baseEvent);
    }

    private ProjectResponseDto mapToDto(Project project) {
        User projectOwner = project.getOwner();

        List<CollaboratorResponseDto> collaborators = Optional.ofNullable(project.getMembers()).orElse(Collections.emptyList())
                .stream()
                .map(member -> CollaboratorResponseDto.builder()
                        .id(member.getUser().getId())
                        .username(member.getUser().getUsername())
                        .email(member.getUser().getEmail())
                        .fullName(member.getUser().getFullName())
                        .role(member.getRole())
                        .build())
                .toList();

        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .user(UserResponseDto.builder()
                        .id(projectOwner.getId())
                        .username(projectOwner.getUsername())
                        .email(projectOwner.getEmail())
                        .fullName(projectOwner.getFullName())
                        .build())
                .createdAt(project.getCreatedAt().format(formatter))
                .updatedAt(project.getUpdatedAt() != null ? project.getUpdatedAt().format(formatter) : null)
                .collaborators(collaborators)
                .build();
    }
}
