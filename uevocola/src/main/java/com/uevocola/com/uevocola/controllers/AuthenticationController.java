package com.uevocola.com.uevocola.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uevocola.com.uevocola.dtos.ResetPasswordDto;
import com.uevocola.com.uevocola.dtos.UserRecordDto;
import com.uevocola.com.uevocola.models.UserModel;
import com.uevocola.com.uevocola.services.UserService;
import com.uevocola.com.uevocola.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRecordDto userRecordDto) {
        try {
            UserModel userModel = userService.saveUser(userRecordDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRecordDto userRecordDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRecordDto.email(), userRecordDto.password())
            );

            String token = jwtUtil.generateToken(userRecordDto.email());
            UserModel userModel = userService.findUserByEmail(userRecordDto.email());

            Map<String, Object> response = new HashMap<>();
            response.put("name", userModel.getName());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciais inválidas"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
    String email = request.get("email");
    try {
        userService.forgotPassword(email);
        return ResponseEntity.ok("Email de redefinição de senha enviado com sucesso!");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


@PostMapping("/reset-password")
public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
    try {
        userService.resetPassword(resetPasswordDto.getToken(), resetPasswordDto.getNewPassword());
        return ResponseEntity.ok("Senha atualizada com sucesso.");
    } catch (RuntimeException e) {
        // Adicionando log para entender o que está acontecendo
        System.out.println("Erro ao redefinir a senha: " + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        // Capturando qualquer outra exceção que possa ocorrer
        System.out.println("Erro inesperado: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado.");
    }
}


}
