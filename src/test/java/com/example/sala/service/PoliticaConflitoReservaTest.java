package com.example.sala.service;

import com.example.sala.dto.ReservaDto;
import com.example.sala.dto.SalaDto;
import com.example.sala.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PoliticaConflitoReservaTest {

    private final PoliticaConflitoReserva politica = new PoliticaConflitoReserva();

    @Test
    void naoDeveriaLancarExcecaoQuandoNaoHouverConflito() {
        // Arrange
        Usuario usuario = criarUsuarioEstudante();
        Sala sala = criarSalaC13();


        List<Reserva> reservasExistentes = List.of(
                criarReservaFake(1L, LocalDateTime.of(2025, 5, 1, 10, 0), LocalDateTime.of(2025, 5, 1, 12, 0), sala, usuario),
                criarReservaFake(2L, LocalDateTime.of(2025, 5, 15, 14, 0), LocalDateTime.of(2025, 5, 15, 17, 0), sala, usuario)
        );


        ReservaDto novaReservaDto = new ReservaDto(16L, 20L,
                LocalDateTime.of(2025, 6, 10, 12, 0),
                LocalDateTime.of(2025, 6, 10, 15, 0), 15, "Atividade de matematica");
        Reserva novaReserva = new Reserva(novaReservaDto, sala, usuario);
        novaReserva.setId(99L);


        assertDoesNotThrow(() -> {
            politica.verificarConflito(novaReserva, reservasExistentes);
        });
    }

    @Test
    void deveriaLancarExcecaoQuandoHouverConflitoDeHorario() {

        // Arrange
        Usuario usuario = criarUsuarioEstudante();
        Sala sala = criarSalaC13();


        List<Reserva> reservasExistentes = List.of(
                criarReservaFake(2L, LocalDateTime.of(2025, 5, 15, 14, 0), LocalDateTime.of(2025, 5, 15, 17, 0), sala, usuario)
        );


        ReservaDto novaReservaDto = new ReservaDto(16L, 20L,
                LocalDateTime.of(2025, 5, 15, 15, 0),
                LocalDateTime.of(2025, 5, 15, 16, 0), 15, "Aula de Reforço");
        Reserva novaReserva = new Reserva(novaReservaDto, sala, usuario);
        novaReserva.setId(99L);

        // Act & Assert
        IllegalStateException excecao = assertThrows(IllegalStateException.class, () -> {
            politica.verificarConflito(novaReserva, reservasExistentes);
        });

        assertTrue(excecao.getMessage().contains("Conflito de horário!"));
    }


    private Usuario criarUsuarioEstudante() {
        Usuario usuario = new Usuario();
        usuario.setId(16L);
        usuario.setUsuario(TipoUsuario.ESTUDANTE);
        usuario.setTelefone("75988062541");
        usuario.setEmail("br@gmail.com");
        usuario.setStatus(Status.ATIVO);
        return usuario;
    }

    private Sala criarSalaC13() {
        SalaDto salaDto = new SalaDto("C13", "Bloco 2", List.of("cadeira", "Mesa"), 50);
        Sala sala = new Sala(salaDto);
        sala.setId(20L);
        return sala;
    }


    private Reserva criarReservaFake(Long idReserva, LocalDateTime inicio, LocalDateTime fim, Sala sala, Usuario usuario) {
        ReservaDto dto = new ReservaDto(usuario.getId(), sala.getId(), inicio, fim, 15, "Atividade antiga");
        Reserva reserva = new Reserva(dto, sala, usuario);
        reserva.setId(idReserva);
        return reserva;
    }

}