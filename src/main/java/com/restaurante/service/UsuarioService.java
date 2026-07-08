package com.restaurante.service;

import com.restaurante.entity.Cliente;
import com.restaurante.entity.Rol;
import com.restaurante.entity.Trabajador;
import com.restaurante.entity.Usuario;
import com.restaurante.repository.ClienteRepository;
import com.restaurante.repository.RolRepository;
import com.restaurante.repository.TrabajadorRepository;
import com.restaurante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    @Autowired
    private RolRepository rolRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ========== REGISTRAR CLIENTE ==========
    @Transactional
    public Usuario registrarCliente(String username, String password, String email,
                                    String nombre, String apellido, String telefono,
                                    String dni, String direccion) {
        
        if (usuarioRepository.existsByUsername(username)) {
            throw new RuntimeException("El usuario ya existe");
        }

        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
            .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setEnabled(true);
        usuario.setRol(rolCliente);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);

        Usuario guardado = usuarioRepository.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setId(guardado.getId());
        cliente.setUsuario(guardado);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setDni(dni);
        cliente.setDireccion(direccion);

        clienteRepository.save(cliente);
        return guardado;
    }

    // ========== REGISTRAR TRABAJADOR ==========
    @Transactional
public Usuario registrarTrabajador(String username, String password, String email,
                                     String nombre, String apellido, String cargo,
                                     String dni, String telefono, String rolNombre) {

    if (usuarioRepository.existsByUsername(username)) {
        throw new RuntimeException("El usuario ya existe");
    }

    Rol rol = rolRepository.findByNombre(rolNombre)
        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));

    Usuario usuario = new Usuario();
    usuario.setUsername(username);
    usuario.setPassword(passwordEncoder.encode(password));
    usuario.setEmail(email);
    usuario.setEnabled(true);
    usuario.setRol(rol);
    usuario.setNombre(nombre);
    usuario.setApellido(apellido);

    Usuario guardado = usuarioRepository.save(usuario);

    Trabajador trabajador = new Trabajador();
    trabajador.setUsuario(guardado); //
    trabajador.setNombre(nombre);
    trabajador.setApellido(apellido);
    trabajador.setCargo(cargo);
    trabajador.setDni(dni);
    trabajador.setTelefono(telefono);
    trabajador.setPuesto(rolNombre);

    trabajadorRepository.save(trabajador);

    return guardado;
    }

    // ========== LOGIN (opcional, si lo usas) ==========
    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }
}