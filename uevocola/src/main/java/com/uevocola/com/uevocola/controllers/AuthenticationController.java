package com.uevocola.com.uevocola.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uevocola.com.uevocola.dtos.UserRecordDto;
import com.uevocola.com.uevocola.models.UserModel;
import com.uevocola.com.uevocola.services.UserService;
import com.uevocola.com.uevocola.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody UserRecordDto userRecordDto) {
    try {
        // Tenta autenticar o usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRecordDto.email(), userRecordDto.password())
        );

        // Gera o token JWT
        String token = jwtUtil.generateToken(userRecordDto.email());

        // Recupera o usuário do banco de dados
        UserModel userModel = userService.findUserByEmail(userRecordDto.email());

        // Prepara a resposta
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

}
