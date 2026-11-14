# MiMercado Ventas

Sistema de gestión de ventas para un supermercado de barrio. Prototipo académico.

## Stack

| Capa | Tecnología | Versión real |
|---|---|---|
| Backend | Spring Boot (API REST) | 3.2.5 |
| Persistencia | H2 en memoria | gestionado por Spring Boot |
| Java | Oracle JDK | 21.0.1 LTS |
| Frontend | React + Vite | React 18 / Vite 5 |
| Enrutamiento | React Router DOM | v6 |
| Gráficos | Recharts | 3.x |
| Node.js / npm | - | v24.20.0 / 11.19.0 |

## Requisitos previos

- **Java 17+** en el PATH (probado con Java 21)
- **Node.js** v18+ y npm (probado con Node v24 / npm 11)
- **Git** (opcional)

## Cómo ejecutar

### 1. Backend (Spring Boot)

```bash
cd backend
# Windows:
mvnw.cmd spring-boot:run
# Linux/Mac:
./mvnw spring-boot:run
```

- API disponible en: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Consola H2 (solo dev): `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:mimercadodb`
  - Usuario: `sa` · Contraseña: (vacía)

### 2. Frontend (React + Vite)

```bash
cd frontend
npm install          # solo la primera vez
npm run dev
```

- Interfaz disponible en: `http://localhost:5173`

> Los datos se cargan automáticamente desde el backend al abrir la app. Ambos servidores deben estar corriendo al mismo tiempo.

## Ejecutar pruebas automatizadas

```bash
cd backend
mvnw.cmd test     # Windows
./mvnw test       # Linux/Mac
```

Resultado esperado: **6 pruebas de integración, 0 fallos**.

## Puertos

| Servicio | Puerto |
|---|---|
| Backend API REST | 8080 |
| Frontend (dev) | 5173 |
| Consola H2 | 8080/h2-console |

## Datos semilla

Al arrancar el backend se cargan automáticamente:
- **5 categorías** y **20 productos** (1 inactivo, 3 con stock bajo, 3 de baja rotación)
- **~60 ventas** de los últimos 40 días (3 anuladas)

> ⚠️ **H2 en memoria:** los datos se reinician al reiniciar el backend.

## Limitaciones conocidas

- Sin autenticación — no apto para datos reales de producción.
- H2 en memoria: sin persistencia entre reinicios.
- Sin IVA, devoluciones parciales, multi-sucursal, lectores de código de barras.
- Concurrencia no optimizada (mitigada con transacciones).
- CORS abierto (`origins = "*"`) — solo para desarrollo.
