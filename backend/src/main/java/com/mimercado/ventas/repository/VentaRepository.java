package com.mimercado.ventas.repository;

import com.mimercado.ventas.model.EstadoVenta;
import com.mimercado.ventas.model.MedioPago;
import com.mimercado.ventas.model.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v WHERE " +
           "(:desde IS NULL OR v.fechaHora >= :desde) AND " +
           "(:hasta IS NULL OR v.fechaHora <= :hasta) AND " +
           "(:medioPago IS NULL OR v.medioPago = :medioPago) AND " +
           "(:estado IS NULL OR v.estado = :estado) " +
           "ORDER BY v.fechaHora DESC")
    Page<Venta> findVentasFiltradas(@Param("desde") LocalDateTime desde,
                                   @Param("hasta") LocalDateTime hasta,
                                   @Param("medioPago") MedioPago medioPago,
                                   @Param("estado") EstadoVenta estado,
                                   Pageable pageable);

    @Query("SELECT v FROM Venta v WHERE " +
           "v.estado = :estado AND " +
           "v.fechaHora >= :desde AND v.fechaHora <= :hasta " +
           "ORDER BY v.fechaHora ASC")
    List<Venta> findVentasEnPeriodo(@Param("desde") LocalDateTime desde,
                                   @Param("hasta") LocalDateTime hasta,
                                   @Param("estado") EstadoVenta estado);
}
