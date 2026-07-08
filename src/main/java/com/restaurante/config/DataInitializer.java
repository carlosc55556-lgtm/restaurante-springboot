package com.restaurante.config;

import com.restaurante.entity.Rol;
import com.restaurante.entity.Usuario;
import com.restaurante.repository.RolRepository;
import com.restaurante.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            // Crear roles
            Rol admin = crearRol(
                    rolRepository,
                    "ADMIN",
                    "Administrador del sistema"
            );

            Rol jefeMozo = crearRol(
                    rolRepository,
                    "JEFE_MOZO",
                    "Jefe de mozos"
            );

            Rol mozo = crearRol(
                    rolRepository,
                    "MOZO",
                    "Mozo del restaurante"
            );

            Rol cliente = crearRol(
                    rolRepository,
                    "CLIENTE",
                    "Cliente del restaurante"
            );


            // Crear usuarios
            crearUsuario(
                    usuarioRepository,
                    passwordEncoder,
                    "Carlos",
                    "Administrador",
                    "admin@gmail.com",
                    "admin",
                    admin
            );


            crearUsuario(
                    usuarioRepository,
                    passwordEncoder,
                    "Juan",
                    "Jefe",
                    "jefe@gmail.com",
                    "jefe",
                    jefeMozo
            );


            crearUsuario(
                    usuarioRepository,
                    passwordEncoder,
                    "Pedro",
                    "Mozo",
                    "mozo@gmail.com",
                    "mozo",
                    mozo
            );


            crearUsuario(
                    usuarioRepository,
                    passwordEncoder,
                    "Ana",
                    "Cliente",
                    "cliente@gmail.com",
                    "cliente",
                    cliente
            );

        };
    }


    private Rol crearRol(
            RolRepository rolRepository,
            String nombre,
            String descripcion
    ) {

        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> {

                    Rol rol = new Rol();

                    rol.setNombre(nombre);
                    rol.setDescripcion(descripcion);

                    return rolRepository.save(rol);
                });
    }


    private void crearUsuario(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            String nombre,
            String apellido,
            String email,
            String username,
            Rol rol
    ) {

        if (usuarioRepository.findByUsername(username).isEmpty()) {

            Usuario usuario = new Usuario();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setUsuario(username);

            // Contraseña: 123456
            usuario.setPassword(
                    passwordEncoder.encode("123456")
            );

            usuario.setRol(rol);

            usuarioRepository.save(usuario);

            System.out.println(
                    "Usuario creado: " + username + " - " + rol.getNombre()
            );
        }
    }
}