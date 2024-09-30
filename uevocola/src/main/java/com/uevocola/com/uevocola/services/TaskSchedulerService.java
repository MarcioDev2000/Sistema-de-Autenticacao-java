package com.uevocola.com.uevocola.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uevocola.com.uevocola.enums.TaskStatus;
import com.uevocola.com.uevocola.models.TaskModel;
import com.uevocola.com.uevocola.producers.UserProducer;
import com.uevocola.com.uevocola.repositories.TaskRepository;

@Service
public class TaskSchedulerService {
   @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserProducer userProducer; // Utilize seu produtor para enviar o e-mail

    @Scheduled(fixedRate = 60000) // Verifica a cada minuto
    public void checkTaskDueDates() {
        List<TaskModel> tasks = taskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (TaskModel task : tasks) {
            if (task.getEndTime() != null && task.getEndTime().isBefore(now) && task.getStatus() != TaskStatus.COMPLETED) {
                // Envie o e-mail quando a tarefa expira, passando tanto o usuário quanto a tarefa
                userProducer.sendTaskCompletionEmail(task.getUser(), task);
                // Atualize o status da tarefa se necessário
                task.setStatus(TaskStatus.COMPLETED);
                taskRepository.save(task);
            }
        }
    }
}
