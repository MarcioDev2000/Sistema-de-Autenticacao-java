package com.uevocola.com.uevocola.models;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.uevocola.com.uevocola.enums.TaskPriority;
import com.uevocola.com.uevocola.enums.TaskStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="tb_task")
public class TaskModel implements Serializable {
     private static final long serialVersionUID = 1L;

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id;

     @NotBlank
     private String title;

     @Enumerated(EnumType.STRING)
     @Column(nullable = false)
     private TaskStatus status;

     @Enumerated(EnumType.STRING)
     @Column(nullable = false)
     private TaskPriority priority;

     private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    private LocalDateTime dueDate; // Data de vencimento
    private LocalDateTime startTime; // Hora de início
    private LocalDateTime endTime; // Hora de término

     public UUID getId() {
        return this.id;
    }
 
    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserModel getUser() {
        return this.user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }


}
