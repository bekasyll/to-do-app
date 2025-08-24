package com.bekassyl.todoapp.service;

import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.model.Task;
import com.bekassyl.todoapp.repository.TaskRepository;
import com.bekassyl.todoapp.util.AppUserNotFoundException;
import com.bekassyl.todoapp.util.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private AppUserService appUserService;

    @Autowired
    public TaskService(TaskRepository taskRepository, AppUserService appUserService) {
        this.taskRepository = taskRepository;
        this.appUserService = appUserService;
    }

    private AppUser getCurrentAppUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserService.getByUsername(username)
                .orElseThrow(() -> new AppUserNotFoundException("User not found!"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findByAppUser(getCurrentAppUser());
    }

    public Task getTaskById(int id) {
        return taskRepository.findByIdAndAppUser(id, getCurrentAppUser())
                .orElseThrow(() -> new TaskNotFoundException("Task not found!"));
    }

    public List<Task> getTasksByCompleted(boolean flag) {
        return taskRepository.findByAppUserAndCompleted(getCurrentAppUser(), flag);
    }

    @Transactional
    public void saveTask(Task task) {
        task.setAppUser(getCurrentAppUser());

        taskRepository.save(task);
    }

    @Transactional
    public void updateTask(int id, Task task) {
        Task existing = taskRepository.findByIdAndAppUser(id, getCurrentAppUser())
                .orElseThrow(() -> new TaskNotFoundException("Task not found!"));

        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setCompleted(task.getCompleted());

        taskRepository.save(existing);
    }

    @Transactional
    public void deleteTask(int id) {
        Task task = taskRepository.findByIdAndAppUser(id, getCurrentAppUser())
                        .orElseThrow(() -> new TaskNotFoundException("Task not found!"));

        taskRepository.delete(task);
    }
}
