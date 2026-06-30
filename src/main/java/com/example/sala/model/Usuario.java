package com.example.sala.model;

import com.example.sala.dto.UsuarioDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "usuario")
public class
Usuario {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotBlank
    @Email
    private String email;

    @Column
    @NotBlank
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?9\\d{4}-?\\d{4}$",
    message = "Formato de celular inválido. Use (XX) 9XXXX-XXXX")
    private String telefone;

    @Column
    @Enumerated(EnumType.STRING)
    private TipoUsuario usuario;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public Usuario(UsuarioDto usuarioDto) {
        this.email = usuarioDto.email();
        this.telefone = usuarioDto.telefone();;
        this.usuario = usuarioDto.usuario();
        this.status = Status.ATIVO;
    }

    public Usuario(String email, String telefone, TipoUsuario usuario, Status status) {
        this.email = email;
        this.telefone = telefone;
        this.usuario = usuario;
        this.status = Status.ATIVO;
    }
}
