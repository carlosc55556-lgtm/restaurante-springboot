package com.restaurante.config;

import com.restaurante.entity.Plato;
import com.restaurante.repository.PlatoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.restaurante.entity.Pedido;
import com.restaurante.entity.Venta;

import com.restaurante.repository.PedidoRepository;
import com.restaurante.repository.VentaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            PlatoRepository platoRepository,
        PedidoRepository pedidoRepository,
        VentaRepository ventaRepository
    ) {

        return args -> {

            crearPlatos(platoRepository);
            crearVentasDemo();
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

public void crearVentasDemo() {

    // =========================
    // VENTA 1 - 02 JULIO
    // =========================

    Pedido pedido1 = new Pedido();
    pedido1.setFecha(LocalDateTime.of(2026, 7, 2, 13, 30));
    pedido1.setEstado("ENTREGADO");
    pedido1.setNombreCliente("Juan Alarcon");
    pedido1.setNumeroMesa(3);

    // agregar detalles del pedido aquí
    // Pollo a la brasa x2
    // Gaseosa x2

    pedidoRepository.save(pedido1);


    Venta venta1 = new Venta();
    venta1.setPedido(pedido1);
    venta1.setFecha(LocalDateTime.of(2026, 7, 2, 14, 00));
    venta1.setSubtotal(80.00);
    venta1.setIgv(14.40);
    venta1.setTotal(94.40);
    venta1.setComprobante("B001-000001");
    venta1.setTipoComprobante("BOLETA");
    venta1.setEstado("PAGADO");

    ventaRepository.save(venta1);



    // =========================
    // VENTA 2 - 06 JULIO
    // =========================

    Pedido pedido2 = new Pedido();
    pedido2.setFecha(LocalDateTime.of(2026, 7, 6, 19, 00));
    pedido2.setEstado("ENTREGADO");
    pedido2.setNombreCliente("Maria Lopez");
    pedido2.setNumeroMesa(7);

    pedidoRepository.save(pedido2);


    Venta venta2 = new Venta();
    venta2.setPedido(pedido2);
    venta2.setFecha(LocalDateTime.of(2026, 7, 6, 19, 40));
    venta2.setSubtotal(150.00);
    venta2.setIgv(27.00);
    venta2.setTotal(177.00);
    venta2.setComprobante("F001-000002");
    venta2.setTipoComprobante("FACTURA");
    venta2.setEstado("PAGADO");

    ventaRepository.save(venta2);



    // =========================
    // VENTA 3 - 10 JULIO
    // =========================

    Pedido pedido3 = new Pedido();
    pedido3.setFecha(LocalDateTime.of(2026, 7, 10, 20, 00));
    pedido3.setEstado("ENTREGADO");
    pedido3.setNombreCliente("Pedro Ramirez");
    pedido3.setNumeroMesa(2);

    pedidoRepository.save(pedido3);


    Venta venta3 = new Venta();
    venta3.setPedido(pedido3);
    venta3.setFecha(LocalDateTime.of(2026, 7, 10, 20, 30));
    venta3.setSubtotal(220.00);
    venta3.setIgv(39.60);
    venta3.setTotal(259.60);
    venta3.setComprobante("F001-000003");
    venta3.setTipoComprobante("FACTURA");
    venta3.setEstado("PAGADO");

    ventaRepository.save(venta3);
}
}