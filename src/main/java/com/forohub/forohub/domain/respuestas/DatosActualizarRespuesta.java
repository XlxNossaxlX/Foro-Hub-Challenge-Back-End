package com.forohub.forohub.domain.respuestas;

public record DatosActualizarRespuesta(
        Long id,
        String mensaje,
        Boolean solucion
) {}
