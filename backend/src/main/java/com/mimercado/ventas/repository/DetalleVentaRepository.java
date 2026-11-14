package com.mimercado.ventas.repository;

import com.mimercado.ventas.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Query("SELECT d FROM DetalleVenta d " +
           "JOIN d.venta v " +
           "WHERE v.estado = com.mimercado.ventas.model.EstadoVenta.COMPLETADA " +
           "AND v.fechaHora >= :desde AND v.fechaHora <= :hasta")
    List<DetalleVenta> findDetallesEnPeriodoCompletadas(@Param("desde") LocalDateTime desde,
                                                       @Param("hasta") LocalDateTime hasta);
}
