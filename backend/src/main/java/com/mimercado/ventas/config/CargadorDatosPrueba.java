package com.mimercado.ventas.config;

import com.mimercado.ventas.model.*;
import com.mimercado.ventas.repository.CategoriaRepository;
import com.mimercado.ventas.repository.ProductoRepository;
import com.mimercado.ventas.repository.VentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class CargadorDatosPrueba {

    @Bean
    CommandLineRunner cargarDatos(CategoriaRepository catRepo,
                                  ProductoRepository prodRepo,
                                  VentaRepository ventaRepo) {
        return args -> {
            // 1. Categorías (5)
            Categoria granos = catRepo.save(new Categoria(null, "Granos y Abarrotes", "Arroz, frijoles, lentejas, azucar"));
            Categoria lacteos = catRepo.save(new Categoria(null, "Lácteos y Huevos", "Leche, quesos, yogures, huevos"));
            Categoria bebidas = catRepo.save(new Categoria(null, "Bebidas y Refrescos", "Gaseosas, jugos, aguas"));
            Categoria aseo = catRepo.save(new Categoria(null, "Aseo y Hogar", "Detergentes, jabones, papel higiénico"));
            Categoria panaderia = catRepo.save(new Categoria(null, "Panadería y Dulces", "Pan tajado, galletas, arepas"));

            // 2. Productos (20)
            // Stock bajo (3 productos: stockActual <= stockMinimo):
            // - Arroz Blanco 1kg (stock 4, min 10)
            // - Leche Entera 1L (stock 3, min 12)
            // - Detergente en Polvo 1kg (stock 2, min 8)
            // Inactivo (1 producto: activo=false):
            // - Galletas Vainilla Paquete (activo false)
            // Baja rotación (3 productos sin ventas en 30 días):
            // - Vinagre Blanco 500ml, Jabón Líquido Manos 300ml, Arepa de Choclo Pack

            List<Producto> productos = new ArrayList<>();
            // Granos
            Producto p1 = new Producto(null, "Arroz Blanco 1kg", granos, new BigDecimal("4500.00"), new BigDecimal("3200.00"), 4, 10, true); // Stock bajo
            Producto p2 = new Producto(null, "Frijol Rojo 500g", granos, new BigDecimal("6800.00"), new BigDecimal("4800.00"), 25, 10, true);
            Producto p3 = new Producto(null, "Lenteja Seleccionada 500g", granos, new BigDecimal("4200.00"), new BigDecimal("3000.00"), 30, 8, true);
            Producto p4 = new Producto(null, "Azúcar Blanca 1kg", granos, new BigDecimal("5200.00"), new BigDecimal("3800.00"), 40, 15, true);
            Producto p5 = new Producto(null, "Vinagre Blanco 500ml", granos, new BigDecimal("3500.00"), new BigDecimal("2200.00"), 20, 5, true); // Baja rotacion (sin ventas)

            // Lácteos
            Producto p6 = new Producto(null, "Leche Entera 1L", lacteos, new BigDecimal("4200.00"), new BigDecimal("3100.00"), 3, 12, true); // Stock bajo
            Producto p7 = new Producto(null, "Huevos AA Cubeta x30", lacteos, new BigDecimal("18500.00"), new BigDecimal("14500.00"), 22, 10, true);
            Producto p8 = new Producto(null, "Queso Campesino 500g", lacteos, new BigDecimal("11000.00"), new BigDecimal("8200.00"), 15, 6, true);
            Producto p9 = new Producto(null, "Mantequilla con Sal 250g", lacteos, new BigDecimal("6500.00"), new BigDecimal("4800.00"), 18, 5, true);

            // Bebidas
            Producto p10 = new Producto(null, "Gaseosa Cola 1.5L", bebidas, new BigDecimal("5500.00"), new BigDecimal("3900.00"), 28, 12, true);
            Producto p11 = new Producto(null, "Agua Mineral sin Gas 600ml", bebidas, new BigDecimal("2500.00"), new BigDecimal("1400.00"), 50, 20, true);
            Producto p12 = new Producto(null, "Jugo de Naranja 1L", bebidas, new BigDecimal("6000.00"), new BigDecimal("4200.00"), 16, 8, true);
            Producto p13 = new Producto(null, "Cerveza Lata 330ml", bebidas, new BigDecimal("3800.00"), new BigDecimal("2600.00"), 45, 15, true);

            // Aseo
            Producto p14 = new Producto(null, "Detergente en Polvo 1kg", aseo, new BigDecimal("9800.00"), new BigDecimal("7000.00"), 2, 8, true); // Stock bajo
            Producto p15 = new Producto(null, "Papel Higiénico 4 rollos", aseo, new BigDecimal("8500.00"), new BigDecimal("6000.00"), 35, 12, true);
            Producto p16 = new Producto(null, "Jabón en Barra Lavaplatos", aseo, new BigDecimal("3200.00"), new BigDecimal("2100.00"), 24, 10, true);
            Producto p17 = new Producto(null, "Jabón Líquido Manos 300ml", aseo, new BigDecimal("7500.00"), new BigDecimal("5000.00"), 15, 5, true); // Baja rotacion (sin ventas)

            // Panadería
            Producto p18 = new Producto(null, "Pan Blanco Tajado 450g", panaderia, new BigDecimal("6200.00"), new BigDecimal("4400.00"), 20, 8, true);
            Producto p19 = new Producto(null, "Arepa de Choclo Pack x5", panaderia, new BigDecimal("5000.00"), new BigDecimal("3500.00"), 12, 5, true); // Baja rotacion (sin ventas)
            Producto p20 = new Producto(null, "Galletas Vainilla Paquete", panaderia, new BigDecimal("3000.00"), new BigDecimal("2000.00"), 10, 5, false); // Inactivo

            productos.addAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20));
            prodRepo.saveAll(productos);

            // 3. Ventas semilla (~60 ventas repartidas en los últimos 45 días)
            // No incluir p5, p17, p19 en ninguna venta de los últimos 30 días para garantizar baja rotación (RN-09).
            // Incluir exactamente 3 ventas con estado ANULADA.
            LocalDateTime ahora = LocalDateTime.now();
            List<Producto> prodParaVenta = List.of(p1, p2, p3, p4, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p18);
            MedioPago[] medios = MedioPago.values();
            Random rng = new Random(42); // Semilla fija para determinismo

            for (int i = 0; i < 60; i++) {
                Venta v = new Venta();
                // Días atrás entre 0 y 40
                int diasAtras = (i * 40) / 60;
                int horasAtras = (i * 7) % 24;
                LocalDateTime fecha = ahora.minusDays(diasAtras).minusHours(horasAtras).minusMinutes(i * 3);
                v.setFechaHora(fecha);
                v.setMedioPago(medios[i % medios.length]);

                // Generar entre 1 y 3 ítems
                int numItems = 1 + (i % 3);
                BigDecimal subtotalVenta = BigDecimal.ZERO;
                for (int j = 0; j < numItems; j++) {
                    Producto prod = prodParaVenta.get((i + j * 3) % prodParaVenta.size());
                    int cantidad = 1 + ((i + j) % 3);
                    BigDecimal subtotalItem = prod.getPrecioVenta().multiply(BigDecimal.valueOf(cantidad));

                    DetalleVenta d = new DetalleVenta();
                    d.setProducto(prod);
                    d.setCantidad(cantidad);
                    d.setPrecioUnitario(prod.getPrecioVenta());
                    d.setCostoUnitario(prod.getCostoUnitario());
                    d.setSubtotal(subtotalItem);

                    v.addDetalle(d);
                    subtotalVenta = subtotalVenta.add(subtotalItem);
                }

                v.setSubtotal(subtotalVenta);
                // Descuento ocasional (por ejemplo cada 7 ventas un descuento de 2000 COP)
                BigDecimal desc = (i % 7 == 0 && subtotalVenta.compareTo(new BigDecimal("5000")) > 0) ? new BigDecimal("2000.00") : BigDecimal.ZERO;
                v.setDescuento(desc);
                v.setTotal(subtotalVenta.subtract(desc));

                // 3 ventas anuladas en posiciones fijas (ej. 5, 20, 45)
                if (i == 5 || i == 20 || i == 45) {
                    v.setEstado(EstadoVenta.ANULADA);
                    v.setFechaAnulacion(fecha.plusHours(1));
                    v.setMotivoAnulacion("Error de digitación o solicitud del cliente");
                } else {
                    v.setEstado(EstadoVenta.COMPLETADA);
                }

                ventaRepo.save(v);
            }
        };
    }
}
