package ida.pe.Invetario.enums;

public enum TipoNotificacion {
    ENTRADA("Entrada de Producto"),
    SALIDA("Salida de Producto"),
    STOCK_BAJO("Stock Bajo"),
    PROXIMO_VENCIMIENTO("Próximo Vencimiento"),
    VENCIMIENTO("Producto Vencido");

    private final String descripcion;

    TipoNotificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
