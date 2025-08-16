package com.forohub.forohub.controller;

import com.forohub.forohub.domain.topico.*;
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
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Transactional
    @PostMapping
    public ResponseEntity<?> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                              UriComponentsBuilder uriComponentsBuilder) {
        if (topicoRepository.existsByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())) {
            return ResponseEntity.status(409).body("Este tópico ya existe en la base de datos.");
        }

        Topico topico = topicoRepository.save(new Topico(datosRegistroTopico));
        DatosListadoTopico datosListadoTopico = new DatosListadoTopico(topico);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosListadoTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable paginacion,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer year
    ) {
        Page<Topico> topicos;
        if (curso != null && year != null) {
            topicos = topicoRepository.findByCursoAndFechaCreacion_Year(curso, year, paginacion);
        } else {
            topicos = topicoRepository.findAll(paginacion);
        }

        Page<DatosListadoTopico> datos = topicos.map(DatosListadoTopico::new);
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoTopico> detallarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosListadoTopico(topico));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosListadoTopico> actualizarTopico(@RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        Topico topico = topicoRepository.getReferenceById(datosActualizarTopico.id());
        topico.actualizarDatos(datosActualizarTopico);
        return ResponseEntity.ok(new DatosListadoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        topicoRepository.delete(topico);
        return ResponseEntity.noContent().build();
    }
}