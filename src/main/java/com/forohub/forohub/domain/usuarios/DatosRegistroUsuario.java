package com.forohub.forohub.domain.usuarios;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(
        @NotBlank
        String login,
        @NotBlank
        String clave
) {}