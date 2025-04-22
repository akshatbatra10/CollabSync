package com.collabsync.backend.service.impl;

import com.collabsync.backend.common.dto.project.ProjectResponseDto;
import com.collabsync.backend.domain.model.Project;
import com.collabsync.backend.repository.ProjectRepository;
import com.collabsync.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponseDto createProject(ProjectResponseDto request, String createdBy) {
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(createdBy)
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdBy(project.getCreatedBy())
                .createdAt(project.getCreatedAt().format(formatter))
                .updatedAt(project.getUpdatedAt() != null ? project.getUpdatedAt().format(formatter) : null)
                .build();
    }
}
