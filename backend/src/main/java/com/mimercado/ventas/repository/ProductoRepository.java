package com.mimercado.ventas.repository;

import com.mimercado.ventas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stockActual <= p.stockMinimo")
    List<Producto> findStockBajo();

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.id NOT IN (" +
           "  SELECT DISTINCT d.producto.id FROM DetalleVenta d " +
           "  JOIN d.venta v " +
           "  WHERE v.estado = com.mimercado.ventas.model.EstadoVenta.COMPLETADA " +
           "  AND v.fechaHora >= :desde" +
           ")")
    List<Producto> findBajaRotacion(@Param("desde") LocalDateTime desde);

    @Query("SELECT p FROM Producto p WHERE " +
           "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
           "(:activo IS NULL OR p.activo = :activo) AND " +
           "(:buscar IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :buscar, '%')))")
    List<Producto> filtrar(@Param("categoriaId") Long categoriaId,
                           @Param("activo") Boolean activo,
                           @Param("buscar") String buscar);
}
