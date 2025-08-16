package com.forohub.forohub.domain.usuarios;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String clave;

    public Usuario(DatosRegistroUsuario datosRegistroUsuario) {
        this.login = datosRegistroUsuario.login();
        this.clave = datosRegistroUsuario.clave();
    }

    public Usuario(String login, String clave) {
        this.login = login;
        this.clave = clave;
    }

    public void actualizarDatos(DatosActualizarUsuario datos, PasswordEncoder passwordEncoder) {
        if (datos.login() != null) {
            this.login = datos.login();
        }
        if (datos.clave() != null) {
            this.clave = passwordEncoder.encode(datos.clave());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}