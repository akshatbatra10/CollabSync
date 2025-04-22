package com.collabsync.backend.service;

import com.collabsync.backend.common.dto.project.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectResponseDto project, String createdBy);

    List<ProjectResponseDto> getProjects();
}
