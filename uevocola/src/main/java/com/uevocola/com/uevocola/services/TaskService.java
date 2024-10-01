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
import com.uevocola.com.uevocola.websocket.MyWebSocketHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private MyWebSocketHandler webSocketHandler;

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
        taskModel.setStatus(TaskStatus.PENDING); // Definindo o status padrão
        taskModel.setPriority(TaskPriority.LOW); // Definindo a prioridade padrão
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

        TaskModel savedTask = taskRepository.save(taskModel);
        
        // Enviar mensagem para os clientes conectados via WebSocket
        webSocketHandler.sendMessage("Nova tarefa adicionada: " + savedTask.getTitle());
        
        return savedTask;
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
            
            // Enviar mensagem para os clientes conectados via WebSocket
            webSocketHandler.sendMessage("Tarefa atualizada: " + taskToUpdate.getTitle());
            return Optional.of(taskToUpdate);
        }
        return Optional.empty(); // Caso a tarefa não seja encontrada
    }

    // Método para deletar uma tarefa
    public boolean deleteTask(UUID id) {
        Optional<TaskModel> taskToDelete = taskRepository.findById(id);

        if (taskToDelete.isPresent()) {
            taskRepository.deleteById(id);
            // Enviar mensagem para os clientes conectados via WebSocket
            webSocketHandler.sendMessage("Tarefa deletada: " + taskToDelete.get().getTitle());
            return true;
        }
        return false;
    }
}
