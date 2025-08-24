package com.bekassyl.todoapp.controller;

import com.bekassyl.todoapp.dto.TaskRequestDTO;
import com.bekassyl.todoapp.dto.TaskResponseDTO;
import com.bekassyl.todoapp.model.Task;
import com.bekassyl.todoapp.service.TaskService;
import com.bekassyl.todoapp.util.TaskNotCreatedException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Autowired
    public TaskController(TaskService taskService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    private Task convertToTask(TaskRequestDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    private TaskRequestDTO convertToTaskRequestDTO(Task task) {
        return modelMapper.map(task, TaskRequestDTO.class);
    }

    private TaskResponseDTO convertToTaskResponseDTO(Task task) {
        return modelMapper.map(task, TaskResponseDTO.class);
    }

    @GetMapping()
    public List<TaskResponseDTO> getAllTasks() {
        return taskService.getAllTasks().stream().map(this::convertToTaskResponseDTO).toList();
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable("id") int id) {
        Task task = taskService.getTaskById(id);

        return convertToTaskResponseDTO(task);
    }

    @GetMapping(params = "completed")
    public List<TaskResponseDTO> getTasksByCompleted(@RequestParam("completed") Boolean flag) {
        return taskService.getTasksByCompleted(flag).stream().map(this::convertToTaskResponseDTO).toList();
    }

    @PostMapping()
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }

            throw new TaskNotCreatedException(errorMsg.toString());
        }

        Task task = convertToTask(taskDTO);
        taskService.saveTask(task);

        return new ResponseEntity<>(convertToTaskResponseDTO(task), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable("id") int id, @RequestBody @Valid TaskRequestDTO taskDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error: errors) {
                errorMsg.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }

            throw new TaskNotCreatedException(errorMsg.toString());
        }

        Task task = convertToTask(taskDTO);
        taskService.updateTask(id, task);

        return new ResponseEntity<>(convertToTaskResponseDTO(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") int id) {
        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}
