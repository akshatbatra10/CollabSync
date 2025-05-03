package com.collabsync.backend.controller;

import com.collabsync.backend.common.dto.task.TaskRequestDto;
import com.collabsync.backend.common.dto.task.TaskResponseDto;
import com.collabsync.backend.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid TaskRequestDto request) {
        TaskResponseDto task = taskService.createTask(request);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping(value = "/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProjectId(@PathVariable Integer projectId) {
        List<TaskResponseDto> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping(value = "/{taskId}/assign")
    public ResponseEntity<String> assignTask(@PathVariable Integer taskId, @RequestParam @NotBlank String username) {
        taskService.assignTask(taskId, username);
        return new ResponseEntity<>("Task assigned", HttpStatus.NO_CONTENT);
    }
}
