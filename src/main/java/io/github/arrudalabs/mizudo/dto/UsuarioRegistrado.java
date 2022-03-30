package io.github.arrudalabs.mizudo.dto;

import io.github.arrudalabs.mizudo.model.Usuario;

public record UsuarioRegistrado(String username, String senha) {
    public UsuarioRegistrado(Usuario usuario) {
        this(usuario.username, "*****");
    }
}