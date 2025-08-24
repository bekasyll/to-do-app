package com.bekassyl.todoapp.repository;

import com.bekassyl.todoapp.model.AppUser;
import com.bekassyl.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByAppUserAndCompleted(AppUser appUser, boolean flag);
    List<Task> findByAppUser(AppUser appUser);
    Optional<Task> findByIdAndAppUser(int id, AppUser appUser);
}
