package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.project.ProjectRequestDto;
import com.collabsync.backend.common.dto.project.ProjectResponseDto;
import com.collabsync.backend.domain.model.Project;
import com.collabsync.backend.domain.model.User;
import com.collabsync.backend.repository.ProjectRepository;
import com.collabsync.backend.service.ProjectService;
import com.collabsync.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
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

    private ProjectResponseDto mapToDto(Project project) {

        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .user(project.getOwner())
                .createdAt(project.getCreatedAt().format(formatter))
                .updatedAt(project.getUpdatedAt() != null ? project.getUpdatedAt().format(formatter) : null)
                .build();
    }
}
