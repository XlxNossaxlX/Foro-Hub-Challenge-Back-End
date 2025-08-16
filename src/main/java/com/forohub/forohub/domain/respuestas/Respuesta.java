package com.forohub.forohub.domain.respuestas;

import com.forohub.forohub.domain.topico.Topico;
import com.forohub.forohub.domain.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private Boolean solucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    public Respuesta(String mensaje, Topico topico, Usuario autor) {
        this.mensaje = mensaje;
        this.topico = topico;
        this.autor = autor;
        this.fechaCreacion = LocalDateTime.now();
        this.solucion = false;
    }

    public void actualizarDatos(DatosActualizarRespuesta datosActualizarRespuesta) {
        if (datosActualizarRespuesta.mensaje() != null) {
            this.mensaje = datosActualizarRespuesta.mensaje();
        }
        if (datosActualizarRespuesta.solucion() != null) {
            this.solucion = datosActualizarRespuesta.solucion();
        }
    }
}