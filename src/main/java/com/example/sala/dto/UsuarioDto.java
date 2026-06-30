package com.example.sala.dto;

import com.example.sala.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;


public record UsuarioDto(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotBlank(message = "O telefone é obrigatório.")
        @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?9\\d{4}-?\\d{4}$", message = "Formato de celular inválido. Use (XX) 9XXXX-XXXX")
        String telefone,

        @NotNull(message = "O tipo de usuário deve ser informado.")
        TipoUsuario usuario) {
}
