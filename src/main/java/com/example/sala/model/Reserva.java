package com.example.sala.model;

import com.example.sala.dto.ReservaDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "reserva")
public class Reserva {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(nullable = false)
    private Integer quantidadePessoas;

    @Column(length = 250)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusReserva statusReserva;

    @Column
    private LocalDateTime dataHoraCriacao;

    public Reserva(ReservaDto reservaDto, Sala sala, Usuario usuario) {

        validarCapacidade(sala, reservaDto.quantidadePessoas());
        validarIntervalo(reservaDto.dataHoraInicio(), reservaDto.dataHoraFim());
        validarSala(sala);


        this.dataHoraCriacao = LocalDateTime.now() ;
        this.usuario = usuario;
        this.sala = sala;
        this.dataHoraInicio = reservaDto.dataHoraInicio();
        this.dataHoraFim = reservaDto.dataHoraFim();
        this.quantidadePessoas = reservaDto.quantidadePessoas();
        this.descricao = reservaDto.descricao();
        this.statusReserva = StatusReserva.PENDENTE;
    }

    private void validarSala(Sala sala){
        if(sala.getStatusDisponibilidade() == StatusDisponibilidade.INATIVA){
            throw new IllegalStateException("Não é possível reservar uma sala inativa.");
        }

    }

    private void  validarCapacidade(Sala sala, Integer quantidadePessoas){
        if (quantidadePessoas==null || quantidadePessoas <=0 || quantidadePessoas > sala.getCapacidadeSala()){
            throw new IllegalArgumentException("Quantidade de pessoas inválida ou acima da capacidade da sala.");
        }

    }

    private void validarIntervalo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim){
        if(dataHoraInicio == null || dataHoraFim == null || !dataHoraInicio.isBefore(dataHoraFim)){
            throw new IllegalArgumentException("As datas são inválidas ou a data de início é depois do fim!");
        }
    }


}
