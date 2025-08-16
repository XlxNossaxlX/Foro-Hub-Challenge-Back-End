package com.forohub.forohub.domain.usuarios;

public record DatosActualizarUsuario(
        Long id,
        String login,
        String clave
) {}