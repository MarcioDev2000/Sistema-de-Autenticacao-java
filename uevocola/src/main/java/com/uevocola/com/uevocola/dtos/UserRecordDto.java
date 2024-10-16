package com.uevocola.com.uevocola.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(@NotBlank String name,
@NotBlank @Email String email,
@NotBlank String password,
@NotBlank String sobrenome,
@NotBlank String nif,
@NotBlank String telefone,
@NotBlank String endereco
) {

}
