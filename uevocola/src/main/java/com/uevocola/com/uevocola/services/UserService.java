package com.uevocola.com.uevocola.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uevocola.com.uevocola.dtos.UserRecordDto;
import com.uevocola.com.uevocola.models.UserModel;
import com.uevocola.com.uevocola.producers.UserProducer;
import com.uevocola.com.uevocola.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProducer userProducer;

    @Transactional
    public UserModel saveUser(UserRecordDto userRecordDto) {
        // Verifica se já existe um usuário com o mesmo email
        if (userRepository.findByEmail(userRecordDto.email()).isPresent()) {
            throw new RuntimeException("Esse email já existe");
        }
        UserModel userModel = new UserModel();
        userModel.setName(userRecordDto.name());
        userModel.setEmail(userRecordDto.email());
        userModel.setPassword(passwordEncoder.encode(userRecordDto.password()));

        UserModel savedUser = userRepository.save(userModel);

        // Enviar email de boas-vindas
        userProducer.sendEmail(savedUser);

        return savedUser;
    }

    public UserModel getUserByEmail(String email) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);
        return userOptional.orElse(null); // Retorna o usuário se encontrado, ou null se não
    }

    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getOneUser(UUID id) {
        return userRepository.findById(id);
    }

    public UserModel updateUser(UUID id, UserRecordDto userRecordDto) {
        UserModel userModel = userRepository.findById(id).orElse(null);
        if (userModel != null) {
            userModel.setName(userRecordDto.name());
            userModel.setEmail(userRecordDto.email());
            // A senha pode ser atualizada conforme a lógica do seu aplicativo
            if (userRecordDto.password() != null && !userRecordDto.password().isEmpty()) {
                userModel.setPassword(passwordEncoder.encode(userRecordDto.password())); // Criptografa a nova senha
            }
            return userRepository.save(userModel);
        }
        return null; // Ou você pode lançar uma exceção
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
