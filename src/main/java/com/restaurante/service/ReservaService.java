package com.restaurante.service;

import com.restaurante.entity.Reserva;
import com.restaurante.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Optional<Reserva> buscarPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }
}
