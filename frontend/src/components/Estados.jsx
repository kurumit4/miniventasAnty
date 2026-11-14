export function EstadoCarga({ texto = 'Cargando...' }) {
  return (
    <div className="estado-carga">
      <div className="spinner" />
      <p>{texto}</p>
    </div>
  );
}

export function EstadoError({ mensaje, onReintentar }) {
  return (
    <div className="estado-error">
      <span className="icono-error">⚠️</span>
      <p>{mensaje || 'Ocurrió un error al cargar los datos.'}</p>
      {onReintentar && (
        <button className="btn btn-secondary" onClick={onReintentar}>
          Reintentar
        </button>
      )}
    </div>
  );
}

export function EstadoVacio({ mensaje = 'No hay datos para mostrar.' }) {
  return (
    <div className="estado-vacio">
      <span className="icono-vacio">📭</span>
      <p>{mensaje}</p>
    </div>
  );
}
