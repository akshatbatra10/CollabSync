package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.project.CollaboratorResponseDto;
import com.collabsync.backend.common.dto.project.ProjectRequestDto;
import com.collabsync.backend.common.dto.project.ProjectResponseDto;
import com.collabsync.backend.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Integer projectId,
                                                           @RequestBody @Valid ProjectRequestDto request) {
        ProjectResponseDto projectResponseDto = projectService.updateProject(projectId, request);

        return ResponseEntity.ok(projectResponseDto);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{projectId}/collaborators")
    public ResponseEntity<?> modifyCollaborator(@PathVariable Integer projectId,
                                                  @RequestParam @NotBlank String username,
                                                  @RequestParam @NotBlank String action) {
        projectService.addOrRemoveCollaborator(projectId, username, action);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/collaborators")
    public ResponseEntity<List<CollaboratorResponseDto>> getCollaborators(@PathVariable Integer projectId) {
        List<CollaboratorResponseDto> collaborators = projectService.getCollaborators(projectId);
        return ResponseEntity.ok(collaborators);
    }
}
