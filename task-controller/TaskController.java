package com.codility.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import com.codility.domain.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;

    @Autowired
    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    // POST /tasks
    @PostMapping
    public ResponseEntity<Long> createTask(@RequestBody TaskDto taskDto) {
        Task task = new Task(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        Task savedTask = repository.save(task);
        return ResponseEntity.ok(savedTask.getId());
    }

    // GET /tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> read(@PathVariable Long id) {
        Optional<Task> taskOpt = repository.findById(id);
        if (!taskOpt.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(taskOpt.get().toDto());
    }

    // PUT /tasks/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody TaskDto taskDto) {
        Optional<Task> taskOpt = repository.findById(id);
        if (!taskOpt.isPresent()) {
            return ResponseEntity.noContent().build();
        }

        Task task = taskOpt.get();

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());

        if (taskDto.getStatus() != null) {
            try {
                TaskStatus status = TaskStatus.valueOf(taskDto.getStatus());
                task.setTaskStatus(status);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest()
                        .body("Available statuses are: CREATED, APPROVED, REJECTED, BLOCKED, DONE.");
            }
        }

        repository.save(task);
        return ResponseEntity.ok().build();
    }

    // DELETE /tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Task> taskOpt = repository.findById(id);
        if (!taskOpt.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        repository.delete(taskOpt.get());
        return ResponseEntity.ok().build();
    }

    // GET /tasks
    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll() {
        List<TaskDto> taskList = new ArrayList<>();
        for (Task task : repository.findAll()) {
            taskList.add(task.toDto());
        }
        return ResponseEntity.ok(taskList);
    }
}