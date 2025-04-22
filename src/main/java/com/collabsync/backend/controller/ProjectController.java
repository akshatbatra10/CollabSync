package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.project.ProjectResponseDto;
import com.collabsync.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody @Valid ProjectResponseDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ProjectResponseDto projectResponseDto = projectService.createProject(request, username);

        return ResponseEntity.ok(projectResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {
        List<ProjectResponseDto> projects = projectService.getProjects();
        return ResponseEntity.ok(projects);
    }
}
