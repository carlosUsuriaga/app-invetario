package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.MovimientoInventario;
import ida.pe.Invetario.Model.Producto;
import ida.pe.Invetario.Repository.MovimientoInventarioRepository;
import ida.pe.Invetario.enums.TipoMovimiento;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoInventarioService {
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoService productoService;
    private final NotificacionService notificacionService;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoRepository,
                                       ProductoService productoService, NotificacionService notificacionService) {
        this.movimientoRepository = movimientoRepository;
        this.productoService = productoService;
        this.notificacionService = notificacionService;
    }


    // Listar todos los movimientos
    public List<MovimientoInventario> listarTodos() {
        return movimientoRepository.findAll();
    }

    // Obtener movimiento por ID
    public MovimientoInventario obtenerPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }

    // Registrar movimiento y actualizar stock
    @Transactional
    public MovimientoInventario registrarMovimiento(MovimientoInventario movimiento) {

        // Obtener producto
        Producto producto = productoService.buscarPorIdProducto(movimiento.getProducto().getIdProducto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));;

        // Actualizar stock según tipo de movimiento
        if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
            producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
        } else if (movimiento.getTipo() == TipoMovimiento.SALIDA) {
            // Validar que hay stock suficiente
            if (producto.getStockActual() < movimiento.getCantidad()) {
                throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStockActual());
            }
            producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
        }

        // Guardar producto actualizado
        productoService.guardarProducto(producto);

        // Guardar movimiento
        MovimientoInventario guardado = movimientoRepository.save(movimiento);
        // ✅ RECARGAR EL PRODUCTO PARA EVITAR LAZY LOADING
        Producto productoActualizado = productoService.buscarPorIdProducto(producto.getIdProducto()).orElse(producto);
        guardado.setProducto(productoActualizado);


        // ✅ AGREGAR ESTO (LO NUEVO)
        try {
            if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
                notificacionService.notificarEntrada(guardado);
            } else if (movimiento.getTipo() == TipoMovimiento.SALIDA) {
                notificacionService.notificarSalida(guardado);
            }
            notificacionService.notificarStockBajo(producto);
            System.out.println("✅ Notificaciones generadas");
        } catch (Exception e) {
            System.err.println("❌ Error notificaciones: " + e.getMessage());
        }

        return guardado;
    }

    // Listar movimientos por producto
    public List<MovimientoInventario> listarPorProducto(Long idProducto) {
        return movimientoRepository.findByProducto_IdProducto(idProducto);
    }

    // Listar movimientos por tipo
    public List<MovimientoInventario> listarPorTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipo(tipo);
    }

    // Listar movimientos entre fechas
    public List<MovimientoInventario> listarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaRegistroBetween(inicio, fin);
    }
    // 🔥 NUEVO: Anular movimiento y revertir stock
    @Transactional
    public void anularMovimiento(Long idMovimiento) {
        // Obtener el movimiento
        MovimientoInventario movimiento = obtenerPorId(idMovimiento);

        // Obtener el producto
        Producto producto = movimiento.getProducto();

        // Revertir el stock según el tipo de movimiento
        if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
            // Si fue ENTRADA, restar lo que se agregó
            if (producto.getStockActual() < movimiento.getCantidad()) {
                throw new RuntimeException("No se puede anular. Stock insuficiente para revertir.");
            }
            producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
        } else if (movimiento.getTipo() == TipoMovimiento.SALIDA) {
            // Si fue SALIDA, sumar lo que se quitó
            producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
        }

        // Guardar producto actualizado
        productoService.guardarProducto(producto);

        // Eliminar el movimiento
        movimientoRepository.delete(movimiento);
    }



}
