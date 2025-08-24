package com.bekassyl.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppUserRequestDTO {
    @NotBlank(message = "Username is required!")
    @Size(min = 1, max = 50)
    private String username;

    @NotBlank(message = "Password is required!")
    private String password;

    public AppUserRequestDTO() {
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
}
