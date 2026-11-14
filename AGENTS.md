# Instrucciones para el agente

Proyecto: **MiMercado Ventas** (gestión de ventas de un supermercado de barrio).

## Empieza aquí
1. Lee `AGENTE-FLUJO-DE-TRABAJO.md`: define las fases, las reglas y las puertas de aprobación (🛑).
2. Lee `PRD.md` (qué se construye) y `SPEC-TECNICA.md` (cómo se construye).
3. Trabaja en la fase indicada por el humano. Si no te dice cuál, empieza por la Fase 0 y registra el avance en `docs/BITACORA.md`.

## Mapa de archivos
| Archivo / carpeta | Uso |
|---|---|
| `PRD.md` | Requisitos y reglas de negocio (borrador, requiere aprobación) |
| `SPEC-TECNICA.md` | Arquitectura, API, modelo de datos (borrador, requiere aprobación) |
| `design.md` | Guía de estilo para Google Stitch |
| `mockups/stitch/` | Mockups exportados de Stitch (los aporta el humano) |
| `backend/`, `frontend/` | Código (Fases 4 y 5) |
| `docs/BITACORA.md` | Registro de decisiones, versiones y problemas |
| `docs/MATRIZ-PRUEBAS.md` | Resultados de pruebas (Fase 7) |
| `docs/HALLAZGOS-CALIDAD.md` | Revisión de calidad (Fase 8) |
| `docs/evidencias/` | Capturas y logs |

## Reglas no negociables
- No escribas código hasta que PRD y spec estén aprobados por el humano.
- Detente en cada puerta 🛑 y espera confirmación explícita.
- No agregues funcionalidades fuera del alcance del PRD.
- No inventes versiones ni puertos; registra los reales.
- Una prueba no ejecutada es `PENDIENTE`.
- Solo datos ficticios; sin secretos en el repositorio.
