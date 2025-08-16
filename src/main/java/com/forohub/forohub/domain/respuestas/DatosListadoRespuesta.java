package com.forohub.forohub.domain.respuestas;

import java.time.LocalDateTime;

public record DatosListadoRespuesta(
        Long id,
        String mensaje,
        LocalDateTime fechaCreacion,
        Boolean solucion,
        Long topicoId,
        String autor
) {
    public DatosListadoRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getSolucion(),
                respuesta.getTopico().getId(),
                respuesta.getAutor().getLogin()
        );
    }
}