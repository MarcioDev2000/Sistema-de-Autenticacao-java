package com.uevocola.com.uevocola.dtos;
import java.time.LocalDateTime;

import com.uevocola.com.uevocola.enums.TaskPriority;
import com.uevocola.com.uevocola.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


public record TaskRecordDto(
    @NotBlank String title,
    @NotNull TaskStatus status, // Adicionando a validação para o status
    @NotNull String description,
    @NotNull UUID userId, // Adicionando a validação para o userId
    TaskPriority priority,
    LocalDateTime dueDate,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}
