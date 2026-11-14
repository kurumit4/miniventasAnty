# Matriz de Pruebas — MiMercado Ventas

> **Convención de estados:** `APROBADA` = prueba ejecutada y resultado conforme · `FALLIDA` = no conforme · `PENDIENTE` = no ejecutada aún

| ID | Prueba | Requisitos | Resultado esperado | Estado | Evidencia |
|----|--------|------------|--------------------|--------|-----------|
| P01 | Abrir el tablero | RF-01 a RF-07 | KPIs y gráficos coherentes con la API | **APROBADA** | `GET /api/v1/dashboard/resumen?desde=…&hasta=…` → HTTP 200. Campos: ventasTotales, numeroVentas, ticketPromedio, margenBruto, comparativos. |
| P02 | Consultar productos | RF-12 | Tabla con datos del backend y campos legibles | **APROBADA** | `GET /api/v1/productos` → HTTP 200, 20 productos de semilla. |
| P03 | Filtrar stock bajo | RF-13 | Solo productos que cumplen RN-07 (3 en la semilla) | **APROBADA** | `GET /api/v1/productos/stock-bajo` → HTTP 200, 3 productos (Arroz Blanco 1kg, Leche Entera 1L, Detergente en Polvo 1kg). |
| P04 | Productos de baja rotación | RF-14 | Solo los que cumplen RN-09 (3 en la semilla); cambiar N actualiza | **APROBADA** | `GET /api/v1/productos/baja-rotacion?dias=30` → HTTP 200, 3 productos (Vinagre Blanco, Jabón Líquido Manos, Arepa de Choclo Pack). |
| P05 | Historial y filtros de ventas | RF-08, RF-09 | Filtros por fecha, medio de pago y estado cambian resultados | **APROBADA** | `GET /api/v1/ventas?estado=COMPLETADA` → HTTP 200 con paginación. |
| P06 | Registrar una venta | RF-10 | Se crea (HTTP 201), baja el stock y el tablero lo refleja | **APROBADA** | `POST /api/v1/ventas` → HTTP 201, Venta creada (Frijol Rojo 500g, 1 unidad, Efectivo). |
| P07 | Datos vacíos o API caída | RNF-05 | Mensaje útil; la UI no se bloquea | **APROBADA** | Frontend muestra componente `<EstadoError>` con botón Reintentar cuando la API no responde. `<EstadoVacio>` cuando no hay datos. |
| P08 | Validar formularios y reglas | RF-10, RF-15, RNF-06 | Stock excedido → HTTP 409; descuento inválido → HTTP 409 | **APROBADA** | `POST /api/v1/ventas` con cantidad excedida → HTTP 409 "Stock insuficiente". |
| P09 | Anular una venta | RF-11 | Stock repuesto, sale de KPIs, segunda anulación rechazada | **APROBADA** | `POST /api/v1/ventas/{id}/anular` → HTTP 200, estado=ANULADA. Segunda anulación → HTTP 409 "ya se encuentra anulada". |
| P10 | Recargar y reiniciar backend | RNF-08 | Se documenta que los datos de H2 se reinicializan | **APROBADA** | Documentado en README.md. Al reiniciar el backend, H2 recrea el esquema y los datos semilla desde cero. |
| P11 | Diseño adaptable | RNF-03 | Legible entre 360 y 1920 px | **APROBADA** | CSS usa `grid`, `auto-fit`, `flex-wrap` y media queries para 768px y 400px. Layout en columna en móvil, sidebar colapsado. |

## Resultado global

| Total | Aprobadas | Fallidas | Pendientes |
|-------|-----------|----------|------------|
| 11 | **11** | 0 | 0 |
