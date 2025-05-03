package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.project.ProjectRequestDto;
import com.collabsync.backend.common.dto.project.ProjectResponseDto;
import com.collabsync.backend.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(@RequestBody @Valid ProjectRequestDto request) {
        ProjectResponseDto projectResponseDto = projectService.createProject(request);

        return ResponseEntity.ok(projectResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getProjects() {
        List<ProjectResponseDto> projects = projectService.getProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{projectId}/collaborators")
    public ResponseEntity<String> addCollaborator(@PathVariable Integer projectId, @RequestParam @NotBlank String username) {
        projectService.addCollaborator(projectId, username);
        return ResponseEntity.ok("Collaborator added");
    }


}
