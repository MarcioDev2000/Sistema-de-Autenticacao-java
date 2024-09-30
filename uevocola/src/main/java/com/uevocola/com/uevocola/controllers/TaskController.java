package com.uevocola.com.uevocola.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uevocola.com.uevocola.dtos.TaskRecordDto;
import com.uevocola.com.uevocola.models.TaskModel;
import com.uevocola.com.uevocola.services.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRecordDto taskRecordDto) {
        try {
            TaskModel taskModel = taskService.createTask(taskRecordDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(taskModel);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks() {
        List<TaskModel> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneTask(@PathVariable(value = "id") UUID id) {
        Optional<TaskModel> taskOptional = taskService.findById(id);

        if (taskOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        return ResponseEntity.ok(taskOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable(value = "id") UUID id,
                                             @RequestBody @Valid TaskRecordDto taskRecordDto) {
        Optional<TaskModel> updatedTask = taskService.updateTask(id, taskRecordDto);
        if (updatedTask.isPresent()) {
            return ResponseEntity.ok(updatedTask.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable(value = "id") UUID id) {
        boolean isDeleted = taskService.deleteTask(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Tarefa deletada com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
    }
}
