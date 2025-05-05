package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.project.CollaboratorResponseDto;
import com.collabsync.backend.common.dto.project.ProjectRequestDto;
import com.collabsync.backend.common.dto.project.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectRequestDto project);

    List<ProjectResponseDto> getProjects();

    ProjectResponseDto getProjectById(Integer projectId);

    void addOrRemoveCollaborator(Integer projectId, String username, String action);

    List<CollaboratorResponseDto> getCollaborators(Integer projectId);

    ProjectResponseDto updateProject(Integer projectId, ProjectRequestDto project);

    void deleteProject(Integer projectId);
}
