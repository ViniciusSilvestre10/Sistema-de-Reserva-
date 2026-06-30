package com.example.sala.service;

import com.example.sala.dto.SalaDto;
import com.example.sala.model.Sala;
import com.example.sala.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaService {

    private final SalaRepository repository;

    public SalaService(SalaRepository repository) {
        this.repository = repository;
    }


    @Transactional(readOnly = true)
    public List<Sala> listarSala() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Sala buscarSalaId(Long id) {
        return  repository.findById(id).orElse(null);
    }

    @Transactional
    public Sala cadastrarSala(SalaDto salaDto) {
        Sala sala = new Sala(salaDto);
        return repository.save(sala);
    }

    @Transactional
    public void deletarSala(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public Sala atualizarSala(Long id, SalaDto salaDto) {
        Sala salaBanco = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id incorreto"));

        salaBanco.setBloco(salaDto.bloco());
        salaBanco.setCapacidadeSala(salaDto.capacidadeSala());
        salaBanco.setNome(salaDto.nome());
        salaBanco.setRecursosDisponivies(salaDto.recursosDisponivies());

        return repository.save(salaBanco);
    }
}
