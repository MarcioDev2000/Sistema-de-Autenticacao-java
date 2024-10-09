package com.uevocola.com.uevocola.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uevocola.com.uevocola.config.JwtUtil;
import com.uevocola.com.uevocola.dtos.UserRecordDto;
import com.uevocola.com.uevocola.models.UserModel;
import com.uevocola.com.uevocola.producers.UserProducer; // Verifique esta importação
import com.uevocola.com.uevocola.repositories.UserRepository;

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
    private UserProducer userProducer; // Verifique se a importação está correta

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public UserModel saveUser(UserRecordDto userRecordDto) {
        // Verifica se já existe um usuário com o mesmo email
        if (userRepository.findByEmail(userRecordDto.email()).isPresent()) {
            throw new RuntimeException("Esse email já existe");
        }
        UserModel userModel = new UserModel();
        userModel.setName(userRecordDto.name());
        userModel.setEmail(userRecordDto.email());
        userModel.setSobrenome(userRecordDto.sobrenome());
        userModel.setTelefone(userRecordDto.telefone()); 
        userModel.setNif(userRecordDto.nif()); 
        userModel.setEndereco(userRecordDto.endereco());
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

    @Transactional
public void forgotPassword(String email) {
    // Encontra o usuário pelo email
    UserModel userModel = userRepository.findByEmail(email).orElseThrow(
        () -> new RuntimeException("Usuário não encontrado com este email.")
    );

    // Gera o token de redefinição de senha
    String resetPasswordToken = jwtUtil.generateResetPasswordToken(email);
    userModel.setResetPasswordToken(resetPasswordToken);

    // Salva o usuário com o token
    userRepository.save(userModel); // Aqui é onde o commit acontece no banco

    // Envia o e-mail com o link de redefinição
    String resetLink = "http://localhost:8082/reset-password?token=" + resetPasswordToken;
    userProducer.sendResetPasswordEmail(userModel.getEmail(), resetLink);
}

    

    @Transactional
public void resetPassword(String token, String newPassword) {
    // Encontra o usuário pelo token
    UserModel user = userRepository.findByResetPasswordToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido ou expirado"));

    // Atualiza a senha do usuário
    user.setPassword(passwordEncoder.encode(newPassword)); // Criptografa a nova senha
    user.setResetPasswordToken(null); // Limpa o token após a redefinição
    userRepository.save(user); // Salva o usuário com a nova senha e o token limpo
}

    
}
