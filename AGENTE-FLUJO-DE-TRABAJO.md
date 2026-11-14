# AGENTE-FLUJO-DE-TRABAJO.md

## Guía operativa para el agente: MiMercado Ventas (gestión de ventas de un supermercado)

**Stack:** Spring Boot (API REST) + React + H2 en memoria · **Diseño:** Google Stitch · **IDE:** Antigravity
**Caso de estudio:** variante opcional de la guía de laboratorio (gestión de ventas de un supermercado de barrio).

> Este documento le indica al agente **qué hacer, en qué orden, qué producir y cuándo detenerse a pedir aprobación humana**.
> Documentos de referencia ya redactados (borradores): `PRD.md` y `SPEC-TECNICA.md`. **La fuente de verdad es siempre la versión aprobada por el humano.**

---

## 0. Reglas generales (leer primero)

1. **Spec-driven:** no escribas código hasta que `PRD.md` y `SPEC-TECNICA.md` estén **aprobados** (casillas de aprobación firmadas por el humano).
2. **Puertas de aprobación (🛑):** al llegar a una, detente, resume lo producido y espera confirmación explícita.
3. **Modo PLAN** en las fases de planeación (1 y 3). No generes código en ellas.
4. **Alcance estricto.** No añadas funcionalidades fuera del PRD. Si crees que falta algo, proponlo y espera aprobación.
5. **Comandos:** antes de ejecutar o instalar algo, explica qué hace. Nada destructivo (`rm -rf`, `git reset --hard`, etc.) sin permiso.
6. **No inventes versiones ni puertos.** Lo que quede en `pom.xml`, `package.json` y `application.properties` es la verdad; regístralo en la spec (sección 2) y el README.
7. **Honestidad en pruebas:** una prueba no ejecutada es `PENDIENTE`, nunca `APROBADA`. Distingue pruebas ejecutadas de las sugeridas.
8. **Datos:** solo ficticios. Sin credenciales, secretos ni datos personales en código, prompts o commits.
9. **Trazabilidad:** cada función importante debe rastrearse a un requisito (`RF-xx`) y a una regla (`RN-xx`) del PRD.
10. **Bitácora:** registra decisiones, desviaciones y problemas en `docs/BITACORA.md`.
11. **Cambios al PRD o a la spec:** si cambias algo ya aprobado, indícalo, actualiza ambos documentos y vuelve a pedir aprobación.

### Fuera de alcance (NO implementar)

- Facturación electrónica y relación con entidades tributarias.
- Contabilidad completa, IVA y cierres contables.
- Pasarelas de pago reales y comercio electrónico.
- Nómina, turnos y gestión de personal.
- Autenticación, roles y permisos.
- Compras y gestión de proveedores.
- Devoluciones parciales y notas crédito (solo existe la anulación total).
- Multi-sucursal, lectores de código de barras y otro hardware.
- Clientes registrados y fidelización (las ventas son anónimas).

---

## 1. Contexto del negocio

Supermercado de barrio que registra ventas de mostrador. El dueño necesita, al cierre del día o del mes, ver **ventas, márgenes, productos más y menos vendidos y medios de pago**, para decidir qué reponer, qué promocionar y cómo va frente al periodo anterior.

> *Como dueño de un supermercado, cuando hago el cierre del día o del mes, quiero ver mis ventas, márgenes, productos más y menos vendidos y medios de pago, para decidir qué productos reponer, cuáles promocionar y cómo va mi negocio frente al periodo anterior.*

### Módulos del MVP

| # | Módulo | Propósito | Funciones clave |
|---|--------|-----------|-----------------|
| M1 | Tablero de ventas | Resumen ejecutivo del periodo | KPIs, comparativo, ventas por día/categoría/medio de pago, Top 5 |
| M2 | Ventas | Registrar y consultar ventas | Nueva venta con validación de stock, historial con filtros, detalle, anulación |
| M3 | Productos y rotación | Catálogo y alertas | Búsqueda y filtros, stock bajo, baja rotación, alta/edición |

### Personas (sin autenticación)

- **Propietario/a (Carlos):** revisa el tablero y la rotación al cierre.
- **Cajero/a (Laura):** registra ventas, consulta el historial y anula errores.

### Reglas de negocio clave (detalle en `PRD.md`, sección 6)

| ID | Resumen |
|----|---------|
| RN-01 | `total = suma(subtotales) − descuento`; `0 <= descuento <= suma(subtotales)` |
| RN-02 | No vender más que el stock; descontar stock en la misma transacción; solo productos activos |
| RN-03 a RN-05 | Ventas totales, ticket promedio y margen bruto, solo sobre ventas `COMPLETADA` |
| RN-06 | Ventas `ANULADA` no cuentan en ningún KPI; anular repone stock |
| RN-07 | Stock bajo: activo y `stock_actual <= stock_minimo` |
| RN-08 | Periodo anterior = mismo número de días inmediatamente anterior |
| RN-09 | Baja rotación: activo y sin ventas completadas en los últimos N días (N = 30) |
| RN-10 a RN-12 | Medios de pago, moneda COP, prorrateo del descuento por categoría |

---

## 2. Estructura objetivo del proyecto

```text
mimercado-ventas/
├── PRD.md
├── design.md                 (opcional, guía de estilo para Stitch)
├── SPEC-TECNICA.md
├── README.md
├── docs/
│   ├── BITACORA.md
│   ├── MATRIZ-PRUEBAS.md
│   ├── HALLAZGOS-CALIDAD.md
│   └── evidencias/           (capturas, logs)
├── mockups/
│   └── stitch/               (ZIP exportado descomprimido: HTML + capturas)
├── backend/                  (Spring Boot)
└── frontend/                 (React + Vite)
```

---

## 3. Flujo de trabajo por fases

```text
F0 Entorno → F1 Revisar PRD 🛑 → F2 Mockups (humano) 🛑 → F3 Revisar spec 🛑
→ F4 Backend → F5 Frontend → F6 Integración
→ F7 Pruebas 🛑 → F8 Calidad → F9 Entrega 🛑
```

---

### FASE 0 · Preparación del entorno

**Tareas**
- [ ] Crear la carpeta del proyecto y la estructura de la sección 2.
- [ ] Copiar `PRD.md` y `SPEC-TECNICA.md` a la raíz si aún no están.
- [ ] Verificar e **imprimir versiones**:
  ```bash
  java -version
  ./mvnw -v        # o mvn -v
  node -v
  npm -v
  git --version
  ```
- [ ] `git init` y `.gitignore` (Java, Node, IDE, `target/`, `node_modules/`, `.env`).
- [ ] Crear `docs/BITACORA.md` con las versiones detectadas.

**Si falta algo:** repórtalo; no instales JDK ni Node sin aprobación del humano.

---

### FASE 1 · Revisión y aprobación del PRD (modo PLAN) 🛑

El PRD ya existe como **borrador v0.1**. Tu trabajo es revisarlo con el humano, no reescribirlo desde cero.

**Rol:** experto en producto.

**Tareas**
- [ ] Leer `PRD.md` completo.
- [ ] Verificar el checklist mínimo:
  - [ ] Requisitos (RF-01 a RF-17) comprensibles y verificables, con criterio de aceptación.
  - [ ] Incluido/excluido diferenciado (sección 4).
  - [ ] Reglas RN-01 a RN-12 sin ambigüedad ni contradicciones (por ejemplo: margen vs. descuento, baja rotación).
  - [ ] RNF con criterios comprobables.
  - [ ] Flujos con inicio, decisiones y resultado (Mermaid).
  - [ ] MVP (P0/P1) separado de mejoras futuras (P2).
- [ ] Listar inconsistencias o vacíos que detectes y proponer correcciones.
- [ ] Presentar al humano las **3 preguntas abiertas** de la sección 10 del PRD:
  1. ¿Se aprueban RN-01 a RN-12, en especial RN-05 (margen) y RN-09 (baja rotación)?
  2. ¿El descuento es solo valor monetario o también porcentaje?
  3. ¿RF-15 y RF-16 entran al MVP o quedan como mejora?
- [ ] Aplicar las decisiones del humano al PRD y subir la versión (v0.2, v1.0).

> Si el humano aprueba, regenera el archivo final:
> *«Genera el PRD definitivo en Markdown y guárdalo como `PRD.md` en la raíz. Conserva las decisiones aprobadas y no agregues funcionalidades fuera del alcance.»*

🛑 **PUERTA 1:** el humano firma la sección 11 del PRD (aprobación). Sin esa firma no se continúa.

---

### FASE 2 · Diseño de interfaces en Google Stitch (**tarea humana**) 🛑

> El agente normalmente **no** opera Stitch. Prepara insumos, guía al humano y valida el resultado.

**El agente hace**
- [ ] (Opcional) Generar `design.md`: paleta, tipografía, espaciado, botones, formularios, tablas, estados (carga, vacío, error, éxito), mensajes y reglas de layout. Sugerencia: estilo profesional y sobrio, buen contraste, formato de moneda COP.
- [ ] Entregar al humano el prompt y los pasos de abajo.

**El humano hace en Stitch**
1. Abrir Stitch e iniciar sesión.
2. Elegir proyecto de **interfaces web** (no móvil/tablet).
3. Elegir un modelo equilibrado entre rapidez y calidad.
4. Definir paleta/estilo (o cargar `design.md`).
5. Cargar `PRD.md` como referencia.
6. Pegar el prompt y refinar con observaciones específicas (distribución, contraste, jerarquía de KPIs, etiquetas, tablas, filtros, navegación).
7. Seleccionar las vistas a conservar → exportar ZIP → descomprimir en `mockups/stitch/`.

**Prompt para Stitch**
> Con base en el PRD adjunto, diseña las vistas principales de una aplicación web para la gestión de ventas de un supermercado de barrio. Incluye: tablero de ventas (selector de periodo, KPIs de ventas totales, número de ventas, ticket promedio y margen, comparativo con el periodo anterior, gráfico de ventas por día, ventas por categoría, medios de pago y Top 5 de productos), historial de ventas con filtros, detalle de venta con opción de anular, formulario de nueva venta con buscador de productos, carrito y total en vivo, listado de productos con alerta de stock bajo, y vista de productos de baja rotación. La interfaz debe ser profesional, clara, usable, accesible y consistente. Prioriza la lectura rápida de KPIs, tablas legibles, filtros comprensibles, estados vacíos, errores y confirmaciones. No añadas módulos fuera del alcance del PRD (sin autenticación, facturación, nómina ni proveedores).

**Vistas mínimas esperadas** (PRD, sección 9): Tablero · Historial de ventas · Detalle de venta · Nueva venta · Productos · Baja rotación.

**El agente valida tras la exportación**
- [ ] `mockups/stitch/` existe con HTML y capturas.
- [ ] Hay una vista por cada fila de la sección 9 del PRD.
- [ ] No hay vistas de módulos fuera de alcance.
- [ ] Anotar en `BITACORA.md` las vistas y archivos.

🛑 **PUERTA 2:** el humano confirma que los mockups están aprobados.
**Evidencia 3:** capturas de las vistas y estructura de la carpeta exportada.

---

### FASE 3 · Revisión y aprobación de la especificación técnica (rol: líder técnico) 🛑

`SPEC-TECNICA.md` ya existe como **borrador v0.1**. Debe reconciliarse con el PRD aprobado y con los mockups.

**Prompt base**
> Eres un ingeniero sénior líder técnico. Revisa `@PRD.md`, `@SPEC-TECNICA.md` y los mockups en `@mockups/stitch/`. Verifica que la especificación sea coherente con el PRD aprobado y con las vistas diseñadas, corrige lo necesario y lista los cambios. Backend: Spring Boot con API REST. Frontend: ReactJS. H2 en memoria con datos de prueba. Sin autenticación.

**Tareas**
- [ ] Reconciliar campos, filtros y vistas de los mockups con la spec; ajustar endpoints o DTO si hace falta.
- [ ] Si el humano cambió reglas en la Fase 1, reflejarlas en la sección 6 de la spec.
- [ ] **Fijar y registrar las versiones reales** (spec, sección 2): Java, Spring Boot, springdoc, Node, React, Vite, etc.
- [ ] Confirmar el modelo de datos (ER), restricciones y los endpoints de la tabla 5.1.
- [ ] Confirmar la semilla: 5 categorías, 20 productos, ~60 ventas, 3 stock bajo, 3 baja rotación, 1 inactivo, 3 anuladas (determinista, fechas relativas a hoy).
- [ ] Revisar el plan de pruebas (P01–P11) y la matriz de trazabilidad.
- [ ] Verificar los criterios de finalización de cada fase I1–I6.

🛑 **PUERTA 3:** el humano firma la sección 13 de la spec y autoriza pasar a código. Si arquitectura, versiones, modelo de datos o rutas son incoherentes, corrígelas **antes** de implementar.

---

### FASE 4 · Implementación del backend (spec I1–I3)

**Orden**
1. **I1 Base:** proyecto Spring Boot en `backend/` (con Maven Wrapper), entidades `Categoria`, `Producto`, `Venta`, `DetalleVenta`, enums `MedioPago` y `EstadoVenta`, repositorios, H2 y `CargadorDatosPrueba` (perfil `dev`).
2. **I2 Lógica:** `VentaService` (registrar y anular, transaccionales), `ProductoService`, `DashboardService` (KPIs, comparativo, series, top, baja rotación), `ManejadorGlobalExcepciones`.
3. **I3 API:** controladores con DTO (no exponer entidades), Bean Validation, códigos HTTP (201/400/404/409), CORS, OpenAPI/Swagger.
4. Pruebas automatizadas de la sección 9.1 de la spec.

**Criterios de finalización**
- [ ] `./mvnw clean test` pasa (o los fallos están documentados).
- [ ] `./mvnw spring-boot:run` arranca sin errores bloqueantes.
- [ ] `GET /api/v1/productos` devuelve los datos semilla.
- [ ] El filtro de stock bajo devuelve 3 productos y baja rotación devuelve 3 (con los datos semilla).
- [ ] Una venta con stock insuficiente responde `409` y **no** modifica nada.
- [ ] Anular repone stock y no se puede anular dos veces.
- [ ] Los KPIs excluyen ventas anuladas.

**Ejemplo de verificación**
```bash
cd backend
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
curl "http://localhost:<PUERTO>/api/v1/dashboard/resumen?desde=2026-11-01&hasta=2026-11-13"
```

---

### FASE 5 · Implementación del frontend (spec I4)

**Orden**
1. App React + Vite en `frontend/`; registrar versiones.
2. `services/` para consumir la API (URL base desde `VITE_API_URL`); los componentes no llaman a `fetch` directamente.
3. Layout y navegación según los mockups.
4. Vistas y rutas: `/` Tablero, `/ventas`, `/ventas/nueva`, `/ventas/:id`, `/productos`, `/productos/baja-rotacion`.
5. Tablero alimentado por la API (**no** valores estáticos).
6. Filtros que realmente cambian los resultados.
7. Formulario de nueva venta: buscador, carrito, total en vivo, validaciones y mensajes del servidor.
8. Estados visibles de **carga, error con reintento y lista vacía** en cada vista.
9. Formato COP con `Intl.NumberFormat('es-CO', ...)`; diseño adaptable (360 a 1920 px) y contraste AA.

**Criterios de finalización**
- [ ] `npm install` y `npm run dev` funcionan.
- [ ] Todas las vistas muestran datos reales del backend.
- [ ] La UI no se bloquea ante error de API ni datos vacíos.
- [ ] Coherencia visual con `mockups/stitch/`.

---

### FASE 6 · Ejecución local e integración (spec I5, parte 1)

1. Levantar el backend (confirmar el puerto real en `application.properties`).
2. Levantar el frontend (usar la URL que imprima el servidor de desarrollo).
3. Verificar conectividad:
   - [ ] Abrir Swagger/OpenAPI.
   - [ ] Ejecutar al menos un `GET` directo a un endpoint real.
   - [ ] Confirmar que el frontend muestra **los mismos datos** que la API.
   - [ ] **Cuadre:** el total del tablero para un periodo coincide con la suma del historial de ventas completadas del mismo periodo.
   - [ ] Revisar la consola del navegador y los logs del backend.
   - [ ] Confirmar que H2 es **en memoria** y los datos se reinicializan al reiniciar.
4. Redactar `README.md`: requisitos, versiones, cómo ejecutar backend y frontend, puertos, ruta de la consola H2 (solo dev), cómo correr pruebas y limitaciones conocidas.

> No expongas la consola H2 fuera de desarrollo.

---

### FASE 7 · Pruebas funcionales y refinamiento (spec I5, parte 2) 🛑

Crea `docs/MATRIZ-PRUEBAS.md` y ejecútala. Estados: `APROBADA`, `FALLIDA`, `PENDIENTE`.

| ID | Prueba | Requisitos | Resultado esperado | Estado | Evidencia |
|----|--------|------------|--------------------|--------|-----------|
| P01 | Abrir el tablero | RF-01 a RF-07 | KPIs y gráficos coherentes con la API | | |
| P02 | Consultar productos | RF-12 | Tabla con datos del backend y campos legibles | | |
| P03 | Filtrar stock bajo | RF-13 | Solo productos que cumplen RN-07 (3 en la semilla) | | |
| P04 | Productos de baja rotación | RF-14 | Solo los que cumplen RN-09 (3 en la semilla); cambiar N actualiza | | |
| P05 | Historial y filtros de ventas | RF-08, RF-09 | Filtros por fecha, medio de pago y estado cambian resultados; detalle correcto | | |
| P06 | Registrar una venta | RF-10 | Se crea, baja el stock y el tablero lo refleja | | |
| P07 | Datos vacíos o API caída | RNF-05 | Mensaje útil; la UI no se bloquea | | |
| P08 | Validar formularios y reglas | RF-10, RF-15, RNF-06 | Obligatorios, stock excedido y descuento inválido bien manejados | | |
| P09 | Anular una venta | RF-11 | Stock repuesto, sale de KPIs, segunda anulación rechazada | | |
| P10 | Recargar y reiniciar backend | RNF-08 | Se documenta que los datos de H2 se reinicializan | | |
| P11 | Diseño adaptable | RNF-03 | Legible entre 360 y 1920 px | | |

**Protocolo ante un fallo (antes de corregir)**
Registra: **ID · pasos para reproducir · resultado actual · resultado esperado · captura/log · hipótesis · corrección aplicada**. Corrige, **vuelve a ejecutar** la prueba y actualiza el estado.

🛑 **PUERTA 4:** presenta la matriz con resultados reales al humano.

---

### FASE 8 · Revisión de calidad del código generado (spec I6)

Crea `docs/HALLAZGOS-CALIDAD.md` evaluando críticamente tu propio código:

| Criterio | Qué verificar |
|----------|---------------|
| **Trazabilidad** | Cada función importante ↔ RF y RN (tabla 11 de la spec) |
| **Separación de responsabilidades** | Controlador / servicio / repositorio / componente con roles claros |
| **Validación** | Reglas validadas en el servidor, no solo en el frontend |
| **Manejo de errores** | Formato único, sin trazas ni datos sensibles |
| **Calidad de datos** | Dinero en `BigDecimal`; KPIs con reglas explícitas; anuladas excluidas; cuadre tablero vs. historial |
| **Transacciones** | Registrar y anular son atómicos; rollback ante error |
| **Seguridad** | Sin secretos en el repo; recordar que sin autenticación no es apto para datos reales |
| **Mantenibilidad** | Nombres, duplicación, complejidad, documentación |
| **Pruebas** | Distinguir ejecutadas vs. sugeridas |
| **Deuda técnica** | Autenticación, H2 → BD persistente, concurrencia, IVA, devoluciones, multi-sucursal |

Para cada hallazgo: descripción, severidad, ubicación, recomendación y estado (corregido / pendiente).

---

### FASE 9 · Entrega 🛑

**Checklist final**
- [ ] `PRD.md` aprobado y consistente con el problema.
- [ ] Mockups exportados en `mockups/stitch/` con capturas.
- [ ] `SPEC-TECNICA.md` actualizado con versiones reales y decisiones aprobadas.
- [ ] Backend compila y arranca sin errores bloqueantes.
- [ ] Frontend instala y arranca correctamente.
- [ ] Al menos una ruta REST probada directamente.
- [ ] Los datos de la interfaz provienen del backend.
- [ ] Matriz de pruebas completa con evidencia y resultados reales.
- [ ] Hallazgos de calidad y limitaciones documentados.
- [ ] `README.md` con instrucciones reproducibles.
- [ ] (Si lo pide el docente) video o demo del flujo principal: abrir tablero → registrar venta → ver el cambio en el tablero → anular → ver stock repuesto.
- [ ] Sin secretos ni datos reales en el repositorio.

🛑 **PUERTA 5:** entrega un resumen final: qué se construyó, qué quedó pendiente, desviaciones respecto al PRD y deuda técnica.

---

## 4. Anti-patrones (no hacer)

- ❌ Escribir código antes de que PRD y spec estén aprobados.
- ❌ «Crear toda la aplicación» de una sola vez.
- ❌ Aceptar sin revisar lo que propone la IA (PRD, spec o código).
- ❌ Asumir versiones o puertos distintos a los realmente configurados.
- ❌ Tablero con valores hardcodeados en el frontend.
- ❌ Lógica de negocio dentro de los controladores o exponer entidades JPA en la API.
- ❌ Usar `double` para dinero.
- ❌ Contar ventas anuladas en KPIs, gráficos o rankings.
- ❌ Descontar stock fuera de una transacción.
- ❌ Marcar pruebas como aprobadas sin haberlas ejecutado.
- ❌ Agregar autenticación, facturación, nómina, proveedores u otros módulos excluidos.
- ❌ Ejecutar comandos destructivos o instalar dependencias sin explicar y obtener permiso.
- ❌ Incluir credenciales, datos personales o información confidencial en prompts o repositorio.

---

## 5. Resumen de puertas de aprobación humana

| Puerta | Momento | El humano aprueba |
|--------|---------|-------------------|
| 🛑 1 | Fin de Fase 1 | `PRD.md` (alcance, RN-01 a RN-12, prioridades) |
| 🛑 2 | Fin de Fase 2 | Mockups exportados |
| 🛑 3 | Fin de Fase 3 | `SPEC-TECNICA.md` y autorización para codificar |
| 🛑 4 | Fin de Fase 7 | Resultados de la matriz de pruebas |
| 🛑 5 | Fin de Fase 9 | Entrega final |

---

## 6. Cambiar de caso de estudio

La guía permite otros casos (activos de una bodega, facturación, nómina de una PYME…). Si el humano lo pide: reescribe `PRD.md` y `SPEC-TECNICA.md` para el nuevo dominio, actualiza las secciones 1 y 3 de este documento (módulos, reglas, semilla, pruebas) y mantén las mismas fases, puertas y entregables.
