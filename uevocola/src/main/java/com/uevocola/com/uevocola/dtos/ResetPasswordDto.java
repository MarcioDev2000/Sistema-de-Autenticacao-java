package com.uevocola.com.uevocola.dtos;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordDto {
    @NotBlank
    private String token;

    @NotBlank
    private String newPassword;

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
