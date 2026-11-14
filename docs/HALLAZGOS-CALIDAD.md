# Hallazgos de Calidad — MiMercado Ventas

> Revisión crítica del código generado según la guía de calidad de la spec técnica.

| # | Criterio | Hallazgo | Severidad | Ubicación | Recomendación | Estado |
|---|----------|----------|-----------|-----------|---------------|--------|
| HQ-01 | **Trazabilidad** | Cada función principal está relacionada con RF y RN. Tabla 11 de la spec cumplida. | ✅ OK | Todos los servicios | - | Conforme |
| HQ-02 | **Separación de responsabilidades** | Controladores solo delegan a servicios y mapean HTTP. Lógica de negocio en servicios. Repositorios solo acceden datos. | ✅ OK | `controller/`, `service/`, `repository/` | - | Conforme |
| HQ-03 | **Validación en servidor** | Bean Validation en DTOs. `VentaService` valida stock (RN-02), descuento (RN-01), producto activo. No depende del frontend. | ✅ OK | `VentaService`, `ProductoService`, DTOs | - | Conforme |
| HQ-04 | **Manejo de errores** | `ManejadorGlobalExcepciones` captura excepciones tipadas y devuelve `ErrorRespuestaDTO` uniforme. No expone stack traces. | ✅ OK | `exception/ManejadorGlobalExcepciones` | Agregar logging con SLF4J en producción | Pendiente (mejora) |
| HQ-05 | **Calidad de datos** | Dinero en `BigDecimal` en todas las entidades y DTOs. Nunca `double` ni `float`. | ✅ OK | `Producto`, `Venta`, `DetalleVenta`, todos los DTOs | - | Conforme |
| HQ-06 | **KPIs correctos** | `DashboardService` filtra explícitamente `EstadoVenta.COMPLETADA`. Ventas anuladas excluidas de todos los KPIs, gráficos y top. | ✅ OK | `DashboardService` | - | Conforme |
| HQ-07 | **Margen bruto (RN-05)** | Calculado como `(ventas_netas - costo_total) / ventas_netas * 100`. Usa costos reales del detalle de venta. | ✅ OK | `DashboardService.obtenerResumen()` | - | Conforme |
| HQ-08 | **Periodo anterior (RN-08)** | Implementado como mismo número de días inmediatamente anterior. | ✅ OK | `DashboardService.obtenerResumen()` | - | Conforme |
| HQ-09 | **Transacciones** | `VentaService.registrarVenta()` y `anularVenta()` son `@Transactional`. Rollback revierte todo. | ✅ OK | `VentaService` | - | Conforme |
| HQ-10 | **Anulación doble (RN-06)** | Se verifica el estado `ANULADA` antes de procesar y lanza `ReglaNegocioException` (409). | ✅ OK | `VentaService.anularVenta()` | - | Conforme |
| HQ-11 | **Stock bajo (RN-07)** | Query JPQL: `activo = true AND stockActual <= stockMinimo`. Flag `stockBajo` en DTO. | ✅ OK | `ProductoRepository`, `ProductoService` | - | Conforme |
| HQ-12 | **Baja rotación (RN-09)** | Subquery excluye productos con ventas `COMPLETADA` en los últimos N días. Configurable. | ✅ OK | `ProductoRepository.findBajaRotacion()` | - | Conforme |
| HQ-13 | **Seguridad** | Sin credenciales en código ni repositorio. Sin autenticación en esta versión monousuario. | ⚠️ Conocido | Controladores | Agregar Spring Security en producción | Pendiente (deuda técnica) |
| HQ-14 | **Consola H2** | Habilitada solo vía `spring.h2.console.enabled=true`. | ⚠️ Menor | `application.properties` | Deshabilitar en producción | Pendiente (deuda técnica) |
| HQ-15 | **Mantenibilidad** | Código limpio, modular y con separación de capas. | ✅ OK | Todo el proyecto | - | Conforme |
| HQ-16 | **Frontend — datos dinámicos** | Tablero consume 100% datos de la API. Sin valores fijos en componentes. | ✅ OK | `Tablero.jsx`, `services/` | - | Conforme |
| HQ-17 | **Formato de moneda** | Formato en COP mediante `Intl.NumberFormat('es-CO')`. | ✅ OK | `utils/format.js` | - | Conforme |
| HQ-18 | **Pruebas** | 6 pruebas de integración ejecutadas con MockMvc. | ✅ OK | `VentasApiIntegrationTests` | - | Conforme |

## Deuda técnica registrada

| ID | Área | Descripción |
|----|------|-------------|
| DT-01 | Seguridad | Sin autenticación ni roles — agregar Spring Security + JWT antes de producción |
| DT-02 | Persistencia | H2 en memoria → migrar a PostgreSQL/MySQL con Flyway en producción |
| DT-03 | Concurrencia | Sin control optimista (`@Version`) para ventas masivas simultáneas |
| DT-04 | Funcionalidad | Sin facturación electrónica/IVA, devoluciones parciales ni multi-sucursal |
