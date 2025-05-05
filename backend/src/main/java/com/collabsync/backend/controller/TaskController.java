package com.collabsync.backend.controller;

import com.collabsync.backend.common.annotations.EnumValidator;
import com.collabsync.backend.common.dto.task.TaskRequestDto;
import com.collabsync.backend.common.dto.task.TaskResponseDto;
import com.collabsync.backend.common.enums.TaskPriority;
import com.collabsync.backend.common.enums.TaskStatus;
import com.collabsync.backend.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping(value = "/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Integer taskId, @RequestBody @Valid TaskRequestDto request) {
        TaskResponseDto task = taskService.updateTask(taskId, request);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping(value = "/{taskId}/assign")
    public ResponseEntity<?> assignTask(@PathVariable Integer taskId, @RequestParam @NotBlank String username) {
        taskService.assignTask(taskId, username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{taskId}/changeStatus")
    public ResponseEntity<Void> changeTaskStatus(@PathVariable Integer taskId,
                                              @RequestParam @NotBlank @EnumValidator(enumClass = TaskStatus.class) String status) {
        taskService.changeTaskStatus(taskId, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{taskId}/changePriority")
    public ResponseEntity<Void> changeTaskPriority(@PathVariable Integer taskId,
                                                   @RequestParam @NotBlank @EnumValidator(enumClass = TaskPriority.class) String priority) {
        taskService.changeTaskPriority(taskId, priority);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
