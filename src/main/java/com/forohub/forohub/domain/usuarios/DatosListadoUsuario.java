package com.forohub.forohub.domain.usuarios;

public record DatosListadoUsuario(
        Long id,
        String login
) {
    public DatosListadoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin());
    }
}