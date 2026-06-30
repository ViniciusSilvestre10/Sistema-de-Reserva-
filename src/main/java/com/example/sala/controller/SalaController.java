package com.example.sala.controller;


import com.example.sala.dto.SalaDto;
import com.example.sala.model.Sala;
import com.example.sala.service.SalaService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sala")
public class SalaController {

    private final SalaService service;

    public SalaController(SalaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Sala>> listarsala(){
        List<Sala> salas =  service.listarSala();
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> buscarSalaId(@PathVariable Long id){
        Sala sala = service.buscarSalaId(id);
        if (sala == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sala);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarSala(@RequestBody SalaDto salaDto){
        Sala novaSala = service.cadastrarSala(salaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaSala);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletarSalaId(@PathVariable Long id){
        service.deletarSala(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarSala(@PathVariable Long id, @RequestBody SalaDto salaDto){
        Sala salaAtualizada = service.atualizarSala(id, salaDto);
        return ResponseEntity.ok(salaAtualizada);
    }


}
