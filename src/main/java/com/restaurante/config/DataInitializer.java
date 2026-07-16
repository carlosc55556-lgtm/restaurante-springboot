package com.restaurante.config;

import com.restaurante.entity.Plato;
import com.restaurante.repository.PlatoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            PlatoRepository platoRepository
    ) {

        return args -> {

            crearPlatos(platoRepository);

        };
    }


    private void crearPlatos(
            PlatoRepository platoRepository
    ) {

        if (platoRepository.count() == 0) {


            platoRepository.save(crearPlato(
                    "Lomo Saltado",
                    "Carne de res salteada con cebolla, tomate, papas fritas y arroz blanco",
                    25.00,
                    "Plato de fondo",
                    50
            ));


            platoRepository.save(crearPlato(
                    "Ají de Gallina",
                    "Pollo deshilachado con crema de ají amarillo acompañado de arroz blanco",
                    18.00,
                    "Plato de fondo",
                    40
            ));


            platoRepository.save(crearPlato(
                    "Ceviche Peruano",
                    "Pescado fresco marinado con limón, cebolla roja, ají y camote",
                    22.00,
                    "Plato de fondo",
                    35
            ));


            platoRepository.save(crearPlato(
                    "Arroz con Pollo",
                    "Arroz verde con culantro acompañado de presa de pollo y salsa criolla",
                    16.00,
                    "Plato de fondo",
                    45
            ));


            platoRepository.save(crearPlato(
                    "Tallarines Verdes",
                    "Pasta con salsa de albahaca peruana acompañada de bistec",
                    17.00,
                    "Plato de fondo",
                    30
            ));


            platoRepository.save(crearPlato(
                    "Causa Rellena",
                    "Papa amarilla sazonada rellena con pollo y mayonesa",
                    15.00,
                    "Entrada",
                    25
            ));


            platoRepository.save(crearPlato(
                    "Anticuchos",
                    "Brochetas de corazón de res marinadas con especias peruanas",
                    14.00,
                    "Parrilla",
                    30
            ));


            platoRepository.save(crearPlato(
                    "Papa a la Huancaína",
                    "Papa sancochada con crema de ají amarillo, queso y leche",
                    10.00,
                    "Entrada",
                    40
            ));


            platoRepository.save(crearPlato(
                    "Chicha Morada",
                    "Bebida tradicional peruana preparada con maíz morado y frutas",
                    6.00,
                    "Bebida",
                    60
            ));


            platoRepository.save(crearPlato(
                    "Limonada",
                    "Limonada natural preparada con limón fresco y azúcar",
                    5.00,
                    "Bebida",
                    60
            ));


            System.out.println("Platos iniciales creados correctamente");

        }

    }


    private Plato crearPlato(
            String nombre,
            String descripcion,
            Double precio,
            String categoria,
            Integer stock
    ) {

        Plato plato = new Plato();

        plato.setNombre(nombre);
        plato.setDescripcion(descripcion);
        plato.setPrecio(precio);
        plato.setCategoria(categoria);
        plato.setStock(stock);
        plato.setDisponible(true);

        return plato;
    }


}