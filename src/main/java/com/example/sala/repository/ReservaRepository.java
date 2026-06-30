package com.example.sala.repository;

import com.example.sala.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findBySalaIdAndDataHoraInicioLessThanAndDataHoraFimAfter(
            Long salaId, LocalDateTime dataHoraFim, LocalDateTime dataHoraInicio
    );
}
