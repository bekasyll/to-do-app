package com.bekassyl.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {
    @NotBlank(message = "Title required")
    @Size(min = 1, max = 50, message = "Title 1–50 chars")
    private String title;

    @Size(max = 150, message = "Description ≤ 150 chars")
    private String description;

    @NotNull(message = "Completion status required")
    private Boolean completed;

    public TaskRequestDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
