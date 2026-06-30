package com.example.sala.controller;


import com.example.sala.dto.UsuarioDto;
import com.example.sala.model.Usuario;
import com.example.sala.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuario(){
        List<Usuario> usuarios =  service.listarUsuario();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioId(@PathVariable Long id){
        Usuario  usuario = service.buscarUsuarioId(id);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarUsuario(@RequestBody UsuarioDto usuarioDto){
        Usuario usuarioNovo = service.cadastrarUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNovo);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deletarUsuarioId(@PathVariable Long id){
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDto usuarioDto){
        Usuario usuarioAtualizado = service.atualizarUsuario(id, usuarioDto);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}
