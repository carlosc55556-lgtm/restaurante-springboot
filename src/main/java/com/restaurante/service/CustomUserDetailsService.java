package com.restaurante.service;

import com.restaurante.entity.Usuario;
import com.restaurante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ← CORREGIDO: findByUsername (no findByUsuario)
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // ← CORREGIDO: getUsername() (no getUsuario())
        // ← CORREGIDO: getRol().getNombre() (Rol es entidad, no enum)
        String role = "ROLE_" + usuario.getRol().getNombre();

        return new User(
            usuario.getUsername(),     // ← CORREGIDO: getUsername()
            usuario.getPassword(),
            usuario.isEnabled(),
            true,
            true,
            true,
            Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}