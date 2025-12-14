package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.MovimientoInventario;
import ida.pe.Invetario.Model.Notificacion;
import ida.pe.Invetario.Model.Producto;
import ida.pe.Invetario.Repository.NotificacionRepository;
import ida.pe.Invetario.Repository.ProductoRepository;
import ida.pe.Invetario.enums.TipoMovimiento;
import ida.pe.Invetario.enums.TipoNotificacion;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificacionService {
    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    // ✅ NOTIFICACIÓN POR ENTRADA
    public void notificarEntrada(MovimientoInventario movimiento) {
        Producto producto = movimiento.getProducto();

        // Si el producto viene sin datos, recargarlo
        if (producto != null && producto.getNombre() == null) {
            // Recargar desde BD si es necesario
            System.out.println("⚠️ Producto sin nombre, recargando...");
        }

        String nombreProducto = (producto != null && producto.getNombre() != null)
                ? producto.getNombre()
                : "Producto desconocido";

        String mensaje = String.format(
                "✅ ENTRADA: Se ingresaron %d unidades de '%s'. Motivo: %s",
                movimiento.getCantidad(),
                nombreProducto,
                movimiento.getMotivo() != null ? movimiento.getMotivo() : "Sin especificar"
        );
        crearNotificacion(producto, mensaje, TipoNotificacion.ENTRADA);
    }

    // ✅ NOTIFICACIÓN POR SALIDA
    public void notificarSalida(MovimientoInventario movimiento) {
        Producto producto = movimiento.getProducto();

        // Si el producto viene sin datos, recargarlo
        if (producto != null && producto.getNombre() == null) {
            // Recargar desde BD si es necesario
            System.out.println("⚠️ Producto sin nombre, recargando...");
        }

        String nombreProducto = (producto != null && producto.getNombre() != null)
                ? producto.getNombre()
                : "Producto desconocido";

        String mensaje = String.format(
                "📤 SALIDA: Se retiraron %d unidades de '%s'. Observación: %s",
                movimiento.getCantidad(),
                nombreProducto,
                movimiento.getObservacion() != null ? movimiento.getObservacion() : "Sin detalles"
        );
        crearNotificacion(producto, mensaje, TipoNotificacion.SALIDA);
    }

    // ✅ NOTIFICACIÓN POR STOCK BAJO
    public void notificarStockBajo(Producto producto) {
        if (producto.getStockActual() <= producto.getStockMinimo()) {
            String mensaje = String.format(
                    "⚠️ STOCK BAJO: '%s' tiene %d unidades. Stock mínimo: %d",
                    producto.getNombre(),
                    producto.getStockActual(),
                    producto.getStockMinimo()
            );
            crearNotificacion(producto, mensaje, TipoNotificacion.STOCK_BAJO);
        }
    }

    // ✅ NOTIFICACIÓN POR VENCIMIENTO PRÓXIMO (2 semanas)
    public void notificarProximoVencimiento(Producto producto) {
        LocalDate hoy = LocalDate.now();
        long diasFaltantes = ChronoUnit.DAYS.between(hoy, producto.getFechaVencimiento());

        if (diasFaltantes <= 14 && diasFaltantes > 0) {
            String mensaje = String.format(
                    "🕐 VENCIMIENTO PRÓXIMO: '%s' vence en %d días (%s)",
                    producto.getNombre(),
                    diasFaltantes,
                    producto.getFechaVencimiento()
            );
            crearNotificacion(producto, mensaje, TipoNotificacion.PROXIMO_VENCIMIENTO);
        }
    }

    // ✅ NOTIFICACIÓN POR PRODUCTO VENCIDO
    public void notificarProductoVencido(Producto producto) {
        if (producto.getFechaVencimiento().isBefore(LocalDate.now())) {
            String mensaje = String.format(
                    "🚨 PRODUCTO VENCIDO: '%s' venció el %s. ¡RETIRAR!",
                    producto.getNombre(),
                    producto.getFechaVencimiento()
            );
            crearNotificacion(producto, mensaje, TipoNotificacion.VENCIMIENTO);
        }
    }

    // ✅ MÉTODO PRIVADO: CREAR NOTIFICACIÓN
    private void crearNotificacion(Producto producto, String mensaje, TipoNotificacion tipo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setProducto(producto);
        notificacion.setMensaje(mensaje);
        notificacion.setTipo(tipo);
        notificacion.setLeido(false);
        notificacionRepository.save(notificacion);
        System.out.println("✅ Notificación creada: " + tipo);
    }

    // ✅ OBTENER TODAS
    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAllByOrderByFechaRegistroDesc();
    }

    // ✅ OBTENER NO LEÍDAS
    public List<Notificacion> obtenerNoLeidas() {
        return notificacionRepository.findByLeidoFalseOrderByFechaRegistroDesc();
    }

    // ✅ OBTENER POR PRODUCTO
    public List<Notificacion> obtenerPorProducto(Long idProducto) {
        return notificacionRepository.findByProductoIdProductoOrderByFechaRegistroDesc(idProducto);
    }

    // ✅ MARCAR COMO LEÍDA
    public Notificacion marcarComoLeida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    // ✅ CONTAR NO LEÍDAS
    public Long contarNoLeidas() {
        return notificacionRepository.countByLeidoFalse();
    }

    // ✅ ELIMINAR
    public void eliminarNotificacion(Integer id) {
        notificacionRepository.deleteById(id);
    }

    // ✅ MARCAR TODAS COMO LEÍDAS
    public void marcarTodoComoLeido() {
        List<Notificacion> noLeidas = obtenerNoLeidas();
        noLeidas.forEach(n -> n.setLeido(true));
        notificacionRepository.saveAll(noLeidas);
    }

}
