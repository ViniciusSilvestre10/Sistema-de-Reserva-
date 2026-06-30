package com.example.sala.service;

import com.example.sala.dto.UsuarioDto;
import com.example.sala.model.Status;
import com.example.sala.model.TipoUsuario;
import com.example.sala.model.Usuario;
import com.example.sala.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void DeverialistarUsuario() {

        Usuario usuario =new Usuario();
        usuario.setUsuario(TipoUsuario.ESTUDANTE);
        usuario.setTelefone("75988062541");
        usuario.setEmail("br@gmail.com");
        usuario.setStatus(Status.ATIVO);

        List<Usuario> listaUsuario = new ArrayList<>();
        listaUsuario.add(usuario);

        when(usuarioRepository.findAll()).thenReturn(listaUsuario);

        List<Usuario> resultado = service.listarUsuario();


        assertEquals(listaUsuario, resultado);
    }

    @Test
    void DeveriaBuscarUsuarioId() {

        Long id = 16L;
        Usuario usuario =new Usuario();
        usuario.setUsuario(TipoUsuario.ESTUDANTE);
        usuario.setTelefone("75988062541");
        usuario.setEmail("br@gmail.com");
        usuario.setStatus(Status.ATIVO);
        usuario.setId(id);


        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.buscarUsuarioId(id);
        assertEquals(usuario, resultado);

    }

    @Test
    void NaoDeveriaBuscarUsuarioId() {

        Long id = 16L;
        Usuario usuario =new Usuario();
        usuario.setUsuario(TipoUsuario.ESTUDANTE);
        usuario.setTelefone("75988062541");
        usuario.setEmail("br@gmail.com");
        usuario.setStatus(Status.ATIVO);
        usuario.setId(id);


        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        Usuario resultado = service.buscarUsuarioId(16L);

        assertNull(resultado);

    }


    @Test
    void DeveriaCadastrarUsuario() {

        UsuarioDto dto =new UsuarioDto("br@gmail.com", "75988062541", TipoUsuario.ESTUDANTE );

        Usuario usuarioSalvo = new Usuario(dto);
        usuarioSalvo.setId(1L);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        Usuario resultado = service.cadastrarUsuario(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(dto.email(), resultado.getEmail());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));


    }

    @Test
    void deletarUsuario() {

        Long id = 16L;

        service.deletarUsuario(id);

        verify(usuarioRepository, times(1)).deleteById(id);

    }

    @Test
    void DeveriaAtualizarUsuario() {

        Long id = 16L;
        UsuarioDto dto =new UsuarioDto("br@gmail.com", "75988062541", TipoUsuario.PROFESSOR);

        Usuario usuarioBanco = new Usuario();
        usuarioBanco.setId(id);
        usuarioBanco.setEmail("antigo@gmail.com");
        usuarioBanco.setTelefone("75900000000");
        usuarioBanco.setUsuario(TipoUsuario.ESTUDANTE);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioBanco));

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioBanco);


        //ACT
        Usuario resultado = service.atualizarUsuario(id, dto);

        //assert

        assertNotNull(resultado);

        assertEquals(usuarioBanco, resultado);

        assertEquals(dto.email(), resultado.getEmail());
        assertEquals(dto.telefone(), resultado.getTelefone());
        assertEquals(dto.usuario(), resultado.getUsuario());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void NaoDeveriaAtualizarUsuarioQuandoIdNaoExistir() {
        // Arrange
        Long idInexistente = 99L;
        UsuarioDto dto = new UsuarioDto("br@gmail.com", "75988062541", TipoUsuario.PROFESSOR);


        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert (Ação e Verificação juntas)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.atualizarUsuario(idInexistente, dto);
        });

        assertEquals("Id incorreto", exception.getMessage());

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }



}