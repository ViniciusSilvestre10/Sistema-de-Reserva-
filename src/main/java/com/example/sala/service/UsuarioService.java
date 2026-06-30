package com.example.sala.service;

import com.example.sala.dto.UsuarioDto;
import com.example.sala.model.Usuario;
import com.example.sala.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuario() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarUsuarioId(Long id) {
        return  repository.findById(id).orElse(null);
    }

    @Transactional
    public Usuario cadastrarUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario(usuarioDto);
        return repository.save(usuario);
    }

    @Transactional
    public void deletarUsuario(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioDto usuarioDto) {
        Usuario usuarioBanco = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id incorreto"));

        usuarioBanco.setUsuario(usuarioDto.usuario());
        usuarioBanco.setEmail(usuarioDto.email());
        usuarioBanco.setTelefone(usuarioDto.telefone());

        return repository.save(usuarioBanco);
    }
}
