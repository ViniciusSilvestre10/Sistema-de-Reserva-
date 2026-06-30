package com.example.sala.service;

import com.example.sala.dto.ReservaDto;
import com.example.sala.model.Reserva;
import com.example.sala.model.Sala;
import com.example.sala.model.Usuario;
import com.example.sala.repository.ReservaRepository;
import com.example.sala.repository.SalaRepository;
import com.example.sala.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final PoliticaConflitoReserva politicaConflitoReserva;

    public ReservaService(ReservaRepository repository, UsuarioRepository usuarioRepository, SalaRepository salaRepository, PoliticaConflitoReserva politicaConflitoReserva) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
        this.politicaConflitoReserva = politicaConflitoReserva;
    }

    @Transactional
    public Reserva criarReserva(ReservaDto reservaDto) {

        List<Reserva> reservasConflitantes = repository.findBySalaIdAndDataHoraInicioLessThanAndDataHoraFimAfter(
                reservaDto.salaId(),
                reservaDto.dataHoraFim(),
                reservaDto.dataHoraInicio()
        );

        Usuario usuario = usuarioRepository.findById(reservaDto.usuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Sala sala = salaRepository.findById(reservaDto.salaId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));

        Reserva novaReserva = new Reserva(reservaDto, sala, usuario);

        politicaConflitoReserva.verificarConflito(novaReserva, reservasConflitantes);

        return repository.save(novaReserva);

    }

    @Transactional
    public void cancelarReserva(Long id) {

        Reserva reserva = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Essa reserva não exites"));

        repository.delete(reserva);

        return;
    }

    public void testeReserva(){
        return;
    }
}
