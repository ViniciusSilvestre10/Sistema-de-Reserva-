package com.example.sala.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SalaDto(
        @NotBlank
        String nome,
        @NotBlank
        String bloco,
        @NotEmpty
        List<String> recursosDisponivies,
        @NotNull
        Integer capacidadeSala) { }
