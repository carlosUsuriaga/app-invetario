package ida.pe.Invetario.enums;

public enum TipoReporte {
    INVENTARIO("Reporte de Inventario", "📊 Estado completo del inventario"),
    STOCK_BAJO("Stock Bajo", "⚠️ Productos con stock por debajo del mínimo"),
    PROXIMOS_VENCER("Próximos a Vencer", "⏰ Productos que vencen en 7 días"),
    VENCIDOS("Productos Vencidos", "🔴 Productos ya vencidos"),
    SIN_MOVIMIENTO("Sin Movimiento", "📦 Productos sin movimiento en 3 semanas"),
    MOVIMIENTOS("Movimientos de Inventario", "🔄 Historial de entradas y salidas"),
    ALERTAS("Reporte de Alertas", "🔔 Notificaciones y alertas del sistema"),
    RESUMEN_GENERAL("Resumen General", "📈 Resumen ejecutivo del inventario");

    private final String titulo;
    private final String descripcion;

    TipoReporte(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
