package com.example.sala.service;

import com.example.sala.model.Reserva;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoliticaConflitoReserva {

    public void verificarConflito(Reserva nova, List<Reserva> existentesLista){

        for (Reserva existente : existentesLista){

            if (existente.getId() != null && existente.getId().equals(nova.getId())){
                continue;
            }

            boolean conflito = nova.getDataHoraInicio().isBefore(existente.getDataHoraFim())
                               && nova.getDataHoraFim().isAfter(existente.getDataHoraInicio());

            if (conflito){
                throw new IllegalStateException(
                        String.format("Conflito de horário! A sala já está reservada por outro usuário no período das %s até %s.",
                                existente.getDataHoraInicio(), existente.getDataHoraFim()));

            }

        }

    }
}
