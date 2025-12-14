package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.*;
import ida.pe.Invetario.Repository.*;
import ida.pe.Invetario.enums.TipoReporte;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {
    private final ReporteRepository reporteRepo;
    private final ProductoRepository productoRepo;
    private final MovimientoInventarioRepository movimientoRepo;
    private final NotificacionRepository notificacionRepo;
    private final UserRepository usuarioRepo;

    public ReporteService(ReporteRepository reporteRepo, ProductoRepository productoRepo, MovimientoInventarioRepository movimientoRepo, NotificacionRepository notificacionRepo, UserRepository usuarioRepo) {
        this.reporteRepo = reporteRepo;
        this.productoRepo = productoRepo;
        this.movimientoRepo = movimientoRepo;
        this.notificacionRepo = notificacionRepo;
        this.usuarioRepo = usuarioRepo;
    }

    /**
     * Genera reporte de inventario completo
     */
    @Transactional
    public Map<String, Object> generarReporteInventario(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            List<Producto> productos = productoRepo.findAll();

            if (productos == null || productos.isEmpty()) {
                datos.put("error", "No hay productos en el sistema");
                return datos;
            }

            datos.put("titulo", "Reporte de Inventario");
            datos.put("tipo", "INVENTARIO");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalProductos", productos.size());
            datos.put("productosActivos", productos.stream().filter(Producto::getEstado).count());

            Integer stockTotal = productos.stream()
                    .mapToInt(Producto::getStockActual)
                    .sum();
            datos.put("stockTotal", stockTotal);

            BigDecimal valorTotal = productos.stream()
                    .map(p -> p.getPrecioUnitario().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            datos.put("valorTotal", valorTotal);

            long stockBajo = productos.stream()
                    .filter(p -> p.getStockActual() <= p.getStockMinimo())
                    .count();
            datos.put("productosStockBajo", stockBajo);

            // ✅ Crear lista limpia de productos
            List<Map<String, Object>> productosLimpio = new ArrayList<>();
            for (Producto p : productos) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("idProducto", p.getIdProducto());
                prod.put("nombre", p.getNombre());
                prod.put("descripcion", p.getDescripcion());
                prod.put("stockActual", p.getStockActual());
                prod.put("stockMinimo", p.getStockMinimo());
                prod.put("precioUnitario", p.getPrecioUnitario());
                prod.put("estado", p.getEstado());

                // Categoría básica
                if (p.getCategoria() != null) {
                    Map<String, Object> categoria = new HashMap<>();
                    categoria.put("idCategoria", p.getCategoria().getIdCategoria());
                    categoria.put("nombre", p.getCategoria().getNombre());
                    prod.put("categoria", categoria);
                }

                productosLimpio.add(prod);
            }

            datos.put("productos", productosLimpio);

            return datos;
        } catch (Exception e) {
            System.err.println("Error generando reporte inventario: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }
    /**
     * Genera reporte de stock bajo
     */
    @Transactional
    public Map<String, Object> generarReporteStockBajo(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            List<Producto> productosBajos = productoRepo.findAll().stream()
                    .filter(p -> p.getStockActual() <= p.getStockMinimo())
                    .collect(Collectors.toList());

            datos.put("titulo", "Productos con Stock Bajo");
            datos.put("tipo", "STOCK_BAJO");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalProductos", productosBajos.size());

            // ✅ Crear lista limpia de productos
            List<Map<String, Object>> productosLimpio = new ArrayList<>();
            for (Producto p : productosBajos) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("idProducto", p.getIdProducto());
                prod.put("nombre", p.getNombre());
                prod.put("stockActual", p.getStockActual());
                prod.put("stockMinimo", p.getStockMinimo());

                // Categoría básica
                if (p.getCategoria() != null) {
                    Map<String, Object> categoria = new HashMap<>();
                    categoria.put("idCategoria", p.getCategoria().getIdCategoria());
                    categoria.put("nombre", p.getCategoria().getNombre());
                    prod.put("categoria", categoria);
                }

                productosLimpio.add(prod);
            }

            datos.put("productos", productosLimpio);

            return datos;
        } catch (Exception e) {
            System.err.println("Error generando reporte stock bajo: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }

    /**
     * Genera reporte de próximos a vencer
     */
    @Transactional
    public Map<String, Object> generarReporteProximosVencer(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            LocalDate hoy = LocalDate.now();
            LocalDate en14Dias = hoy.plusDays(14);

            List<Producto> proximosVencer = productoRepo.findAll().stream()
                    .filter(p -> p.getFechaVencimiento() != null &&
                            p.getFechaVencimiento().isAfter(hoy) &&
                            p.getFechaVencimiento().isBefore(en14Dias) || p.getFechaVencimiento().equals(en14Dias))
                    .sorted(Comparator.comparing(Producto::getFechaVencimiento))
                    .collect(Collectors.toList());

            datos.put("titulo", "Productos Próximos a Vencer");
            datos.put("tipo", "PROXIMOS_VENCER");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalProductos", proximosVencer.size());

            // ✅ Crear lista limpia de productos
            List<Map<String, Object>> productosLimpio = new ArrayList<>();
            for (Producto p : proximosVencer) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("idProducto", p.getIdProducto());
                prod.put("nombre", p.getNombre());
                prod.put("stockActual", p.getStockActual());
                prod.put("fechaVencimiento", p.getFechaVencimiento());

                // Categoría básica
                if (p.getCategoria() != null) {
                    Map<String, Object> categoria = new HashMap<>();
                    categoria.put("idCategoria", p.getCategoria().getIdCategoria());
                    categoria.put("nombre", p.getCategoria().getNombre());
                    prod.put("categoria", categoria);
                }

                productosLimpio.add(prod);
            }

            datos.put("productos", productosLimpio);

            return datos;
        } catch (Exception e) {
            System.err.println("Error generando reporte próximos a vencer: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }
    /**
     * Genera reporte de productos vencidos
     */
    @Transactional
    public Map<String, Object> generarReporteVencidos(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            LocalDate hoy = LocalDate.now();

            List<Producto> vencidos = productoRepo.findAll().stream()
                    .filter(p -> p.getFechaVencimiento() != null && p.getFechaVencimiento().isBefore(hoy))
                    .sorted(Comparator.comparing(Producto::getFechaVencimiento))
                    .collect(Collectors.toList());

            datos.put("titulo", "Productos Vencidos");
            datos.put("tipo", "VENCIDOS");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalProductos", vencidos.size());

            // ✅ Crear lista limpia de productos
            List<Map<String, Object>> productosLimpio = new ArrayList<>();
            for (Producto p : vencidos) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("idProducto", p.getIdProducto());
                prod.put("nombre", p.getNombre());
                prod.put("stockActual", p.getStockActual());
                prod.put("fechaVencimiento", p.getFechaVencimiento());

                // Categoría básica
                if (p.getCategoria() != null) {
                    Map<String, Object> categoria = new HashMap<>();
                    categoria.put("idCategoria", p.getCategoria().getIdCategoria());
                    categoria.put("nombre", p.getCategoria().getNombre());
                    prod.put("categoria", categoria);
                }

                productosLimpio.add(prod);
            }

            datos.put("productos", productosLimpio);

            return datos;
        } catch (Exception e) {
            System.err.println("Error generando reporte vencidos: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }
    /**
     * Genera reporte de productos sin movimiento
     */
    @Transactional
    public Map<String, Object> generarReporteSinMovimiento(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            LocalDateTime hace3Semanas = LocalDateTime.now().minusWeeks(3);

            List<Producto> sinMovimiento = productoRepo.findAll().stream()
                    .filter(p -> p.getFechaActualizacion() != null &&
                            p.getFechaActualizacion().isBefore(hace3Semanas))
                    .sorted(Comparator.comparing(Producto::getFechaActualizacion))
                    .collect(Collectors.toList());

            datos.put("titulo", "Productos Sin Movimiento");
            datos.put("tipo", "SIN_MOVIMIENTO");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalProductos", sinMovimiento.size());

            // ✅ Crear lista limpia de productos
            List<Map<String, Object>> productosLimpio = new ArrayList<>();
            for (Producto p : sinMovimiento) {
                Map<String, Object> prod = new HashMap<>();
                prod.put("idProducto", p.getIdProducto());
                prod.put("nombre", p.getNombre());
                prod.put("stockActual", p.getStockActual());
                prod.put("fechaActualizacion", p.getFechaActualizacion());

                // Categoría básica
                if (p.getCategoria() != null) {
                    Map<String, Object> categoria = new HashMap<>();
                    categoria.put("idCategoria", p.getCategoria().getIdCategoria());
                    categoria.put("nombre", p.getCategoria().getNombre());
                    prod.put("categoria", categoria);
                }

                productosLimpio.add(prod);
            }

            datos.put("productos", productosLimpio);

            return datos;
        } catch (Exception e) {
            System.err.println("Error generando reporte sin movimiento: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }

    /**
     * Genera reporte de movimientos de inventario
     */
    @Transactional
    public Map<String, Object> generarReporteMovimientos(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            List<MovimientoInventario> movimientos = movimientoRepo.findAll();

            if (movimientos == null) {
                movimientos = new ArrayList<>();
            }

            // Contar entradas y salidas
            int entradas = 0;
            int salidas = 0;

            for (MovimientoInventario m : movimientos) {
                if (m.getTipo() != null && m.getTipo().name().equals("ENTRADA")) {
                    entradas++;
                } else if (m.getTipo() != null && m.getTipo().name().equals("SALIDA")) {
                    salidas++;
                }
            }

            datos.put("titulo", "Reporte de Movimientos de Inventario");
            datos.put("tipo", "MOVIMIENTOS");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalMovimientos", movimientos.size());
            datos.put("entradas", entradas);
            datos.put("salidas", salidas);

            // ✅ Crear lista limpia de movimientos sin relaciones circulares
            List<Map<String, Object>> movimientosLimpio = new ArrayList<>();
            for (MovimientoInventario m : movimientos) {
                Map<String, Object> mov = new HashMap<>();
                mov.put("idMovimiento", m.getIdMovimiento());
                mov.put("tipo", m.getTipo() != null ? m.getTipo().name() : "DESCONOCIDO");
                mov.put("cantidad", m.getCantidad());
                mov.put("motivo", m.getMotivo());
                mov.put("observacion", m.getObservacion());
                mov.put("fechaRegistro", m.getFechaRegistro());

                // Producto básico sin relaciones
                if (m.getProducto() != null) {
                    Map<String, Object> producto = new HashMap<>();
                    producto.put("idProducto", m.getProducto().getIdProducto());
                    producto.put("nombre", m.getProducto().getNombre());
                    mov.put("producto", producto);
                }

                // Usuario básico
                if (m.getUsuario() != null) {
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("nombre", m.getUsuario().getNombre());
                    mov.put("usuario", usuario);
                }

                movimientosLimpio.add(mov);
            }

            datos.put("movimientos", movimientosLimpio);

            return datos;

        } catch (Exception e) {
            System.err.println("Error generando reporte movimientos: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al generar reporte: " + e.getMessage());
            return error;
        }
    }

    /**
     * Genera reporte de alertas y notificaciones
     */
    @Transactional
    public Map<String, Object> generarReporteAlertas(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();

            List<Notificacion> notificaciones = notificacionRepo.findAll();

            if (notificaciones == null) {
                notificaciones = new ArrayList<>();
            }

            long noLeidas = notificacionRepo.countByLeidoFalse();

            datos.put("titulo", "Reporte de Alertas");
            datos.put("tipo", "ALERTAS");
            datos.put("fecha", LocalDateTime.now());
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));
            datos.put("totalAlertas", notificaciones.size());
            datos.put("alertasNoLeidas", noLeidas);
            datos.put("alertasLeidas", notificaciones.size() - noLeidas);

            // ✅ Crear lista limpia de notificaciones sin relaciones circulares
            List<Map<String, Object>> notificacionesLimpio = new ArrayList<>();
            for (Notificacion n : notificaciones) {
                Map<String, Object> notif = new HashMap<>();
                notif.put("idNotificacion", n.getIdNotificacion());
                notif.put("mensaje", n.getMensaje());
                notif.put("tipo", n.getTipo() != null ? n.getTipo().name() : "INFO");
                notif.put("leido", n.getLeido());
                notif.put("fechaRegistro", n.getFechaRegistro());

                // Producto básico
                if (n.getProducto() != null) {
                    Map<String, Object> producto = new HashMap<>();
                    producto.put("idProducto", n.getProducto().getIdProducto());
                    producto.put("nombre", n.getProducto().getNombre());
                    notif.put("producto", producto);
                }

                notificacionesLimpio.add(notif);
            }

            datos.put("notificaciones", notificacionesLimpio);

            return datos;

        } catch (Exception e) {
            System.err.println("Error generando reporte alertas: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al generar reporte: " + e.getMessage());
            return error;
        }
    }

    /**
     * Genera resumen general del sistema
     */
    @Transactional
    public Map<String, Object> generarResumenGeneral(Long idUsuario) {
        try {
            Map<String, Object> datos = new HashMap<>();
            List<Producto> productos = productoRepo.findAll();

            Integer stockTotal = productos.stream()
                    .mapToInt(Producto::getStockActual)
                    .sum();

            BigDecimal valorTotal = productos.stream()
                    .map(p -> p.getPrecioUnitario().multiply(BigDecimal.valueOf(p.getStockActual())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal precioPromedio = productos.stream()
                    .map(Producto::getPrecioUnitario)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Math.max(productos.size(), 1)), 2, java.math.RoundingMode.HALF_UP);

            long alertasNoLeidas = notificacionRepo.countByLeidoFalse();

            datos.put("totalProductos", productos.size());
            datos.put("productosActivos", productos.stream().filter(Producto::getEstado).count());
            datos.put("stockTotal", stockTotal);
            datos.put("valorTotal", valorTotal);
            datos.put("precioPromedio", precioPromedio);
            datos.put("alertasNoLeidas", alertasNoLeidas);

            long stockBajo = productos.stream()
                    .filter(p -> p.getStockActual() <= p.getStockMinimo())
                    .count();
            long proximosVencer = productos.stream()
                    .filter(p -> p.getFechaVencimiento() != null &&
                            p.getFechaVencimiento().isBefore(LocalDate.now().plusDays(7)) &&
                            p.getFechaVencimiento().isAfter(LocalDate.now()))
                    .count();
            long vencidos = productos.stream()
                    .filter(p -> p.getFechaVencimiento() != null &&
                            p.getFechaVencimiento().isBefore(LocalDate.now()))
                    .count();

            datos.put("productosStockBajo", stockBajo);
            datos.put("productosProximosVencer", proximosVencer);
            datos.put("productosVencidos", vencidos);
            datos.put("generadoPor", obtenerNombreUsuario(idUsuario));

            return datos;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }

    /**
     * Guarda un reporte en la BD
     */
    @Transactional
    private void guardarReporte(String titulo, TipoReporte tipo, Long idUsuario,
                                int totalRegistros, int productosActivos) {
        try {
            Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
            if (usuario == null) return;

            Reporte reporte = new Reporte();
            reporte.setTitulo(titulo);
            reporte.setTipo(tipo);
            reporte.setUsuario(usuario);
            reporte.setTotalRegistros(totalRegistros);
            reporte.setProductosActivos(productosActivos);
            reporte.setProductosInactivos(totalRegistros - productosActivos);

            reporteRepo.save(reporte);
        } catch (Exception e) {
            System.err.println("Error guardando reporte: " + e.getMessage());
        }
    }

    /**
     * Obtiene el nombre del usuario
     */
    private String obtenerNombreUsuario(Long idUsuario) {
        try {
            if (idUsuario == null) return "Sistema";
            Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
            return usuario != null ? usuario.getNombre() + " " + usuario.getApellido() : "Desconocido";
        } catch (Exception e) {
            return "Error al obtener usuario";
        }
    }

    /**
     * Obtiene historial de reportes
     */
    public List<Reporte> obtenerHistorialReportes() {
        try {
            return reporteRepo.findAllByOrderByFechaRegistroDesc();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
