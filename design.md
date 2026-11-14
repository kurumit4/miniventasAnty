# design.md: Guía de estilo para Google Stitch

Proyecto: **MiMercado Ventas**, aplicación web (escritorio primero, adaptable a 360 px).
Tono visual: profesional, sobrio, cercano. Prioriza la lectura rápida de cifras.

## 1. Principios
1. Las cifras importantes (KPIs) se leen en un vistazo.
2. Una acción principal por pantalla.
3. Tablas legibles antes que decoración.
4. Estados siempre visibles: carga, vacío, error y éxito.
5. Accesibilidad AA: contraste de texto ≥ 4.5:1, foco visible, etiquetas en todos los campos.

## 2. Paleta (propuesta, ajustable en Stitch)
| Uso | Color | Hex |
|---|---|---|
| Primario (acciones, enlaces) | Verde mercado | `#1B7F4C` |
| Primario oscuro (hover) | Verde profundo | `#14633B` |
| Secundario (acentos de gráficos) | Azul petróleo | `#1F6F8B` |
| Fondo de página | Gris muy claro | `#F5F7F6` |
| Superficie (tarjetas, tablas) | Blanco | `#FFFFFF` |
| Texto principal | Gris casi negro | `#1F2933` |
| Texto secundario | Gris medio | `#52606D` |
| Borde | Gris claro | `#D9E0DC` |
| Éxito | Verde | `#2E7D32` |
| Advertencia (stock bajo) | Ámbar | `#B45309` |
| Error / anulada | Rojo | `#B91C1C` |

Los estados nunca dependen solo del color: acompáñalos con texto o ícono (por ejemplo «Stock bajo», «Anulada»).

## 3. Tipografía
- Familia: sans-serif legible (por ejemplo Inter o Roboto).
- Títulos de página 24 px, secciones 18 px, texto 14 a 16 px, cifras de KPI 28 a 32 px en negrita.
- Cifras alineadas a la derecha en tablas; fuente con números tabulares si es posible.

## 4. Espaciado y layout
- Rejilla de 8 px. Márgenes de contenido 24 px (escritorio), 16 px (móvil).
- Navegación lateral fija en escritorio, menú colapsable en móvil.
- Menú: Tablero · Ventas · Nueva venta · Productos · Baja rotación.
- Tarjetas con borde de 1 px y radio de 8 px; sin sombras pesadas.

## 5. Componentes
| Componente | Reglas |
|---|---|
| **Botón primario** | Fondo primario, texto blanco, altura 40 px. Uno por vista. |
| **Botón secundario** | Borde primario, fondo transparente. |
| **Botón destructivo** (Anular) | Rojo; siempre con diálogo de confirmación. |
| **Campos** | Etiqueta arriba, ayuda y error debajo, borde rojo + texto en error. |
| **Tarjeta KPI** | Título pequeño, cifra grande, variación vs. periodo anterior (▲ verde / ▼ rojo con texto). |
| **Tabla** | Encabezado fijo, filas con separador fino, paginación abajo, filtros arriba. |
| **Insignias** | `COMPLETADA` verde, `ANULADA` roja, `Stock bajo` ámbar, `Inactivo` gris. |
| **Gráficos** | Línea para ventas por día; barras para categorías; dona o barras para medios de pago. Con leyenda y valores accesibles. |
| **Selector de periodo** | Atajos (Hoy, 7 días, Mes actual, 30 días) + rango personalizado. |

## 6. Estados (obligatorios en cada vista)
| Estado | Comportamiento |
|---|---|
| Carga | Esqueleto o indicador; no bloquear el resto de la pantalla. |
| Vacío | Mensaje claro y, si aplica, acción sugerida (ej. «No hay ventas en este periodo»). |
| Error | Mensaje comprensible y botón «Reintentar». |
| Éxito | Confirmación breve tras registrar o anular una venta. |

## 7. Formato de datos
- Moneda COP con separador de miles: `$ 12.400`.
- Fechas `13/11/2026`, horas `10:25`.
- Porcentajes con un decimal: `24,9 %`.

## 8. Vistas a diseñar (PRD, sección 9)
1. **Tablero**: selector de periodo, 4 KPIs (ventas totales, nº de ventas, ticket promedio, margen), comparativo, gráfico por día, ventas por categoría, medios de pago, Top 5.
2. **Historial de ventas**: filtros (fechas, medio de pago, estado), tabla paginada.
3. **Detalle de venta**: datos, ítems, descuento, total, botón Anular.
4. **Nueva venta**: buscador de productos, carrito, medio de pago, descuento, total en vivo, confirmar.
5. **Productos**: búsqueda, filtros por categoría y stock bajo, indicador de stock, crear/editar.
6. **Baja rotación**: selector de N días y lista de productos sin ventas.

## 9. Fuera de alcance (no diseñar)
Inicio de sesión, facturación, nómina, proveedores, pasarelas de pago, clientes, devoluciones parciales, multi-sucursal.
