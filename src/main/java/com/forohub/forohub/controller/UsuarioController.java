package com.forohub.forohub.controller;

import com.forohub.forohub.domain.usuarios.DatosActualizarUsuario;
import com.forohub.forohub.domain.usuarios.DatosListadoUsuario;
import com.forohub.forohub.domain.usuarios.DatosRegistroUsuario;
import com.forohub.forohub.domain.usuarios.Usuario;
import com.forohub.forohub.domain.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosListadoUsuario> registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                                                UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario.login(),
                passwordEncoder.encode(datosRegistroUsuario.clave())));
        DatosListadoUsuario datosListadoUsuario = new DatosListadoUsuario(usuario);
        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(datosListadoUsuario);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuario>> listarUsuarios(@PageableDefault(size = 10) Pageable paginacion) {
        Page<Usuario> usuarios = usuarioRepository.findAll(paginacion);
        Page<DatosListadoUsuario> datos = usuarios.map(DatosListadoUsuario::new);
        return ResponseEntity.ok(datos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoUsuario> detallarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosListadoUsuario(usuario));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosListadoUsuario> actualizarUsuario(@RequestBody @Valid DatosActualizarUsuario datosActualizarUsuario) {
        Usuario usuario = usuarioRepository.getReferenceById(datosActualizarUsuario.id());
        usuario.actualizarDatos(datosActualizarUsuario, passwordEncoder);
        return ResponseEntity.ok(new DatosListadoUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}