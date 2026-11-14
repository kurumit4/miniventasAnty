# Bitácora del proyecto

## Versiones del entorno (Fase 0)
| Herramienta | Versión detectada |
|---|---|
| Java | 21.0.1 (Oracle LTS, 64-Bit Server VM) |
| Maven | No instalado en PATH global (se utilizó Maven Wrapper `./mvnw` en `backend/`) |
| Node.js | v24.20.0 |
| npm | 11.19.0 |
| Git | 2.49.0.windows.1 |

## Registro
| Fecha | Fase | Decisión / evento | Responsable |
|---|---|---|---|
| 2026-11-13 | Fase 0 | Verificación e inicialización de entorno (`git init`, registro de versiones de herramientas). Estructura validada. | Agente |
| 2026-11-13 | Fase 1 | Aprobación de PRD v1.0 con reglas RN-01 a RN-12, descuento monetario en COP, e inclusión de RF-15/RF-16. Puerta 1 superada. | Usuario / Agente |
| 2026-11-13 | Fase 2 | Verificación y validación de mockups exportados en `mockups/stitch/` (Tablero, Historial/Detalle, Nueva Venta, Catálogo, Baja Rotación). Puerta 2 superada. | Usuario / Agente |
| 2026-11-13 | Fase 3 | Reconciliación técnica entre PRD, Mockups y Spec Técnica v1.0. Registro de versiones reales (Java 21, Spring Boot 3.2.5, React 18). Puerta 3 superada. Inicio Fase 4. | Usuario / Agente |
| 2026-11-13 | Fase 4 | Implementación del backend Spring Boot completada (I1-I3). Pruebas de integración automatizadas ejecutadas: 6 pasadas, 0 fallos. | Agente |
| 2026-11-13 | Fase 5 | Implementación del frontend React+Vite completada. 6 vistas (Tablero, Historial, Detalle, Nueva Venta, Productos, Baja Rotación). | Agente |
| 2026-11-13 | Fase 6 | Backend en puerto 8080 ✅. Frontend en puerto 5173 ✅. Verificación API: GET /productos/stock-bajo → 200 OK con 3 productos. README.md generado. | Agente |
| 2026-11-13 | Fase 7 | Matriz de pruebas ejecutada: 11/11 APROBADAS (P01-P11). Verificaciones con PowerShell + inspección de código. | Agente |
| 2026-11-13 | Fase 8 | HALLAZGOS-CALIDAD.md generado: 18 criterios revisados, 15 conformes, 3 con deuda técnica documentada. | Agente |
| 2026-11-13 | Fase 9 | Entrega final completada y consolidada en Git. Puerta 5 alcanzada. Proyecto listo para revisión docente/evaluación. | Agente |

## Desviaciones respecto al PRD o la spec
_Ninguna._

## Problemas encontrados
- Maven no estaba en el PATH global; se resolvió usando Maven Wrapper (`mvnw` / `mvnw.cmd`) en `backend/`.
- El archivo `mockups/stitch/Antigravity IDE.exe` (190MB) excedía el límite de subida de GitHub (100MB), por lo que se agregó a `.gitignore`.
