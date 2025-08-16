package com.forohub.forohub.controller;

import com.forohub.forohub.domain.respuestas.*;
import com.forohub.forohub.domain.topico.TopicoRepository;
import com.forohub.forohub.domain.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosListadoRespuesta> registrarRespuesta(@RequestBody @Valid DatosRegistroRespuesta datosRegistroRespuesta,
                                                                    UriComponentsBuilder uriComponentsBuilder) {
        var topico = topicoRepository.getReferenceById(datosRegistroRespuesta.topicoId());
        var autor = usuarioRepository.getReferenceById(datosRegistroRespuesta.autorId());

        Respuesta respuesta = respuestaRepository.save(new Respuesta(datosRegistroRespuesta.mensaje(), topico, autor));
        DatosListadoRespuesta datosListadoRespuesta = new DatosListadoRespuesta(respuesta);

        URI url = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(url).body(datosListadoRespuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoRespuesta>> listarRespuestas(@PageableDefault(size = 10) Pageable paginacion) {
        Page<Respuesta> respuestas = respuestaRepository.findAll(paginacion);
        Page<DatosListadoRespuesta> datos = respuestas.map(DatosListadoRespuesta::new);
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoRespuesta> detallarRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosListadoRespuesta(respuesta));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosListadoRespuesta> actualizarRespuesta(@RequestBody @Valid DatosActualizarRespuesta datosActualizarRespuesta) {
        Respuesta respuesta = respuestaRepository.getReferenceById(datosActualizarRespuesta.id());
        respuesta.actualizarDatos(datosActualizarRespuesta);
        return ResponseEntity.ok(new DatosListadoRespuesta(respuesta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Long id) {
        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}