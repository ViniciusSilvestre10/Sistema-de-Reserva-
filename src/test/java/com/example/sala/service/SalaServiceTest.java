package com.example.sala.service;

import com.example.sala.dto.SalaDto;
import com.example.sala.model.*;
import com.example.sala.repository.SalaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository repository;

    @InjectMocks
    private SalaService service;



    @Test
    void deverialistarSala() {

        Sala sala1 =new Sala();
        sala1.setNome("B12");
        sala1.setCapacidadeSala(50);
        sala1.setBloco("Bloco 2");
        sala1.setRecursosDisponivies(List.of("cadeira", "mesa"));
        sala1.setStatusDisponibilidade(StatusDisponibilidade.ATIVA);

        List<Sala> sala = List.of(sala1);

        when(repository.findAll()).thenReturn(sala);

        List<Sala> resultado = service.listarSala();


        assertEquals(sala, resultado);
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveriaBuscarSalaId() {

        Long id = 16L;
        Sala sala1 =new Sala();
        sala1.setNome("B12");
        sala1.setCapacidadeSala(50);
        sala1.setBloco("Bloco 2");
        sala1.setRecursosDisponivies(List.of("cadeira", "mesa"));
        sala1.setStatusDisponibilidade(StatusDisponibilidade.ATIVA);
        sala1.setId(id);


        when(repository.findById(id)).thenReturn(Optional.of(sala1));

        Sala resultado = service.buscarSalaId(id);
        assertEquals(sala1, resultado);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void NaoDeveriaBuscarSalaId() {
        Long id = 16L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        Sala resultado = service.buscarSalaId(id);

        assertNull(resultado);
    }

    @Test
    void DeveriacadastrarSala() {

        SalaDto salaDto = new SalaDto("C13", "Bloco 2", List.of("cadeira", "Mesa"), 50);
        Sala salaEsperada = new Sala(salaDto);

        when(repository.save(any(Sala.class))).thenReturn(salaEsperada);

        Sala resultado = service.cadastrarSala(salaDto);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(salaEsperada.getNome(), resultado.getNome());
        Assertions.assertEquals(salaEsperada.getBloco(), resultado.getBloco());

        verify(repository, times(1)).save(any(Sala.class));
    }

    @Test
    void deveriaAtualizarSalaComSucesso() {
        // Arrange
        Long idExistente = 1L;
        SalaDto dtoComNovosDados = new SalaDto("B15", "Bloco 3", List.of("Projetor"), 60);

        Sala salaAntigaNoBanco = new Sala();
        salaAntigaNoBanco.setId(idExistente);
        salaAntigaNoBanco.setNome("B12");
        salaAntigaNoBanco.setBloco("Bloco 2");

         when(repository.findById(idExistente)).thenReturn(Optional.of(salaAntigaNoBanco));


        when(repository.save(any(Sala.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Sala resultado = service.atualizarSala(idExistente, dtoComNovosDados);

        // Assert
        assertNotNull(resultado);
        assertEquals("B15", resultado.getNome());
        assertEquals("Bloco 3", resultado.getBloco());
        assertEquals(60, resultado.getCapacidadeSala());

        verify(repository, times(1)).findById(idExistente);
        verify(repository, times(1)).save(any(Sala.class));
    }

    @Test
    void deveriaLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        Long idInexistente = 999L;
        SalaDto dto = new SalaDto("B15", "Bloco 3", List.of("Projetor"), 60);


        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            service.atualizarSala(idInexistente, dto);
        });

        assertEquals("Id incorreto", excecao.getMessage());


        verify(repository, times(1)).findById(idInexistente);
        verify(repository, never()).save(any(Sala.class));
    }
}