package com.example.gmail.gmail_test.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gmail.gmail_test.Models.EmailModel;

public interface EmailRepository  extends JpaRepository<EmailModel, UUID> {

}
