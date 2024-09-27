package com.uevocola.com.uevocola.producers;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.uevocola.com.uevocola.dtos.EmailDto;
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
}

