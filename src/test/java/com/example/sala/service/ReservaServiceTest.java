package com.example.sala.service;

import com.example.sala.dto.ReservaDto;
import com.example.sala.dto.SalaDto;
import com.example.sala.dto.UsuarioDto;
import com.example.sala.model.Reserva;
import com.example.sala.model.Sala;
import com.example.sala.model.TipoUsuario;
import com.example.sala.model.Usuario;
import com.example.sala.repository.ReservaRepository;
import com.example.sala.repository.SalaRepository;
import com.example.sala.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private PoliticaConflitoReserva politicaConflitoReserva;

    @Test
    void deveriaCriarReserva() {

        LocalDateTime inicio = LocalDateTime.now().plusDays(1);
        LocalDateTime fim = inicio.plusHours(2);

        ReservaDto novaReserva = new ReservaDto(
                1L,
                10L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(2),
                5,
                "Reunião de alinhamento do time de Dev");


        SalaDto salaDto = new SalaDto("Sala 01", "Bloco A", List.of("Projetor"), 10);
        Sala salaMock = new Sala(salaDto);


        UsuarioDto usuarioDto = new UsuarioDto("dev@email.com", "(75) 99999-9999", TipoUsuario.ESTUDANTE);
        Usuario usuarioMock = new Usuario(usuarioDto);

        when( repository.findBySalaIdAndDataHoraInicioLessThanAndDataHoraFimAfter(
                novaReserva.salaId(),
                novaReserva.dataHoraFim(),
                novaReserva.dataHoraInicio()
        )).thenReturn(Collections.emptyList());

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(salaRepository.findById(10L)).thenReturn(Optional.of(salaMock));


        when(repository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reserva reservaCriada = reservaService.criarReserva(novaReserva);

        // ASSERT
        assertNotNull(reservaCriada, "A reserva criada não deveria ser nula");


        verify(repository).findBySalaIdAndDataHoraInicioLessThanAndDataHoraFimAfter(
                eq(10L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
        verify(usuarioRepository).findById(1L);
        verify(salaRepository).findById(10L);
        verify(politicaConflitoReserva).verificarConflito(any(Reserva.class), anyList());
        verify(repository).save(any(Reserva.class));

    }

    @Test
    void cancelarReservaCenarioSucesso() {
        // ARRANGE
        Long idReserva = 1L;
        Reserva reservaMock = new Reserva();


        when(repository.findById(idReserva)).thenReturn(Optional.of(reservaMock));

        // ACT
        assertDoesNotThrow(() -> reservaService.cancelarReserva(idReserva));

        // ASSERT
        verify(repository).findById(idReserva);
        verify(repository).delete(reservaMock);
    }

    @Test
    void cancelarReservaCenarioFalhaIdNaoEncontrado() {
        // ARRANGE
        Long idInexistente = 99L;

        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        // ACT & ASSERT
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservaService.cancelarReserva(idInexistente);
        });

        assertEquals("Essa reserva não exites", exception.getMessage());

        verify(repository, never()).delete(any(Reserva.class));
    }
}