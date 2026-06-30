package com.example.sala.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record ReservaDto(
        @NotNull(message = "O ID do usuário é obrigatório, velho.")
        Long usuarioId,

        @NotNull(message = "O ID da sala é obrigatório.")
        Long salaId,

        @NotNull(message = "A data de início deve ser informada.")
        @FutureOrPresent(message = "A data de início não pode ser no passado.")
        LocalDateTime dataHoraInicio,

        @NotNull(message = "A data de fim deve ser informada.")
        @Future(message = "A data de fim deve ser uma data futura.")
        LocalDateTime dataHoraFim,

        @NotNull(message = "A quantidade de pessoas é obrigatória.")
        @Positive(message = "A quantidade de pessoas deve ser maior que zero.")
        Integer quantidadePessoas,

        String descricao
) {

}