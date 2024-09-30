package com.uevocola.com.uevocola.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uevocola.com.uevocola.dtos.TaskRecordDto;
import com.uevocola.com.uevocola.enums.TaskPriority;
import com.uevocola.com.uevocola.enums.TaskStatus;
import com.uevocola.com.uevocola.models.TaskModel;
import com.uevocola.com.uevocola.models.UserModel;
import com.uevocola.com.uevocola.repositories.TaskRepository;
import com.uevocola.com.uevocola.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Método para listar todas as tarefas
    public List<TaskModel> findAll() {
        return taskRepository.findAll();
    }

    // Método para buscar uma tarefa pelo ID
    public Optional<TaskModel> findById(UUID id) {
        return taskRepository.findById(id);
    }

    // Método para criar uma nova tarefa
    public TaskModel createTask(TaskRecordDto taskRecordDto) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTitle(taskRecordDto.title());
        taskModel.setDescription(taskRecordDto.description());
        taskModel.setStatus(taskRecordDto.status());
        taskModel.setPriority(taskRecordDto.priority()); // Definindo a prioridade
        taskModel.setStartTime(taskRecordDto.startTime()); // Definindo a data e hora de início
        taskModel.setEndTime(taskRecordDto.endTime()); // Definindo a data e hora de término
        taskModel.setDueDate(taskRecordDto.dueDate());
        // Buscar o usuário pelo UUID fornecido
        Optional<UserModel> userOptional = userRepository.findById(taskRecordDto.userId());
        if (userOptional.isPresent()) {
            taskModel.setUser(userOptional.get()); // Definir o usuário
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
              // Define o status e Priority por padrão
         taskModel.setStatus(TaskStatus.PENDING);
         taskModel.setPriority(TaskPriority.LOW);

        return taskRepository.save(taskModel);
    }



    // Método para atualizar uma tarefa existente
    public Optional<TaskModel> updateTask(UUID id, TaskRecordDto taskRecordDto) {
        Optional<TaskModel> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            TaskModel taskToUpdate = existingTask.get();
            taskToUpdate.setTitle(taskRecordDto.title());
            taskToUpdate.setPriority(taskRecordDto.priority()); // Definindo a prioridade
            taskToUpdate.setStartTime(taskRecordDto.startTime()); // Definindo a data e hora de início
            taskToUpdate.setEndTime(taskRecordDto.endTime()); // Definindo a data e hora de término
            taskToUpdate.setDueDate(taskRecordDto.dueDate());
            taskRepository.save(taskToUpdate);
            return Optional.of(taskToUpdate);
        }
        return Optional.empty(); // Caso a tarefa não seja encontrada
    }

    // Método para deletar uma tarefa
    public boolean deleteTask(UUID id) {
        Optional<TaskModel> taskToDelete = taskRepository.findById(id);

        if (taskToDelete.isPresent()) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
