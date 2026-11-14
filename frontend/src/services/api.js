// Servicio base que todos los demás servicios importan
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

async function request(path, options = {}) {
  const url = `${BASE_URL}${path}`;
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  });
  if (!res.ok) {
    let errorMsg = `Error ${res.status}`;
    try {
      const json = await res.json();
      errorMsg = json.mensaje || json.message || errorMsg;
    } catch (_) {}
    throw new Error(errorMsg);
  }
  if (res.status === 204) return null;
  return res.json();
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: 'POST', body: JSON.stringify(body) }),
  put: (path, body) => request(path, { method: 'PUT', body: JSON.stringify(body) }),
  patch: (path, body) => request(path, { method: 'PATCH', body: JSON.stringify(body) }),
};
