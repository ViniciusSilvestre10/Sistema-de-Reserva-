package com.example.sala.controller;

import com.example.sala.dto.ReservaDto;
import com.example.sala.model.Reserva;
import com.example.sala.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserva")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping("/criar")
    public ResponseEntity<Reserva> criarReserva(@Valid @RequestBody ReservaDto reservaDto){
        Reserva reserva = service.criarReserva(reservaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancelarReserva(@PathVariable Long id){
        service.cancelarReserva(id);
        return ResponseEntity.noContent().build();
    }


}
