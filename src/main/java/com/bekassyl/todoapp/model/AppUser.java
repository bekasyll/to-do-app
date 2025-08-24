package com.bekassyl.todoapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @NotBlank(message = "Username is required!")
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password is required!")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "active_token")
    private String activeToken;

    @OneToMany(mappedBy = "appUser")
    private List<Task> tasks = new ArrayList<>();

    public AppUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActiveToken() {
        return activeToken;
    }

    public void setActiveToken(String jwtToken) {
        this.activeToken = jwtToken;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
