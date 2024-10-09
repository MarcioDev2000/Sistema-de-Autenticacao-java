package com.uevocola.com.uevocola.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.uevocola.com.uevocola.dtos.EmailDto;
import com.uevocola.com.uevocola.models.TaskModel;
import com.uevocola.com.uevocola.models.UserModel;

@Component
public class UserProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    public void sendEmail(UserModel user) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(user.getId());
        emailDto.setEmailTo(user.getEmail());
        emailDto.setSubject("Bem-vindo ao Uevocola");
        emailDto.setText("Olá " + user.getName() + ",\n\nObrigado por se cadastrar no Uevocola!");

        rabbitTemplate.convertAndSend(queue, emailDto);
    }

    public void sendTaskCompletionEmail(UserModel user, TaskModel task) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(user.getId());
        emailDto.setEmailTo(user.getEmail());
        emailDto.setSubject("Sua tarefa '" + task.getTitle() + "' foi concluída");
        emailDto.setText("Olá " + user.getName() + ",\n\nA tarefa '" + task.getTitle() + "' que você registrou atingiu seu prazo e foi concluída.");

        rabbitTemplate.convertAndSend(queue, emailDto);
    }

    public void sendResetPasswordEmail(String email, String recoveryLink) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(email);
        emailDto.setSubject("Recuperação de Senha");
        emailDto.setText("Olá,\n\nVocê solicitou a recuperação da sua senha. Acesse o link para redefinir sua senha: " + recoveryLink);

        rabbitTemplate.convertAndSend(queue, emailDto);
    }
}