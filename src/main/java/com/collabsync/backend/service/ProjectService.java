package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.project.CollaboratorResponseDto;
import com.collabsync.backend.common.dto.project.ProjectRequestDto;
import com.collabsync.backend.common.dto.project.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectRequestDto project);

    List<ProjectResponseDto> getProjects();

    void addCollaborator(Integer projectId, String username);
}
