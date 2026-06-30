package com.example.sala.model;

import com.example.sala.dto.SalaDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "sala")
public class Sala {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false, length = 100)
    private String nome;

    @Column
    private String bloco;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "sala_recursos", joinColumns = @JoinColumn(name = "sala_id"))
    @Column(name = "recursos")
    private List<String> recursosDisponivies;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusDisponibilidade statusDisponibilidade;

    @Column(name = "capacidade_sala", nullable = false)
    private Integer capacidadeSala;


    public Sala(SalaDto salaDto) {
        this.bloco = salaDto.bloco();
        this.nome = salaDto.nome();
        this.recursosDisponivies = salaDto.recursosDisponivies();
        this.statusDisponibilidade = StatusDisponibilidade.ATIVA;
        this.capacidadeSala = salaDto.capacidadeSala();
    }
}
