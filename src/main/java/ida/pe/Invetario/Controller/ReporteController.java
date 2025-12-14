package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Reporte;
import ida.pe.Invetario.Repository.*;
import ida.pe.Invetario.Service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reporte")
public class ReporteController {
    private final ReporteService reporteService;
    private final ReporteRepository reporteRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final NotificacionRepository notificacionRepository;

    public ReporteController(ReporteService reporteService, ReporteRepository reporteRepository, ProductoRepository productoRepository, ProveedorRepository proveedorRepository, CategoriaRepository categoriaRepository, NotificacionRepository notificacionRepository) {
        this.reporteService = reporteService;
        this.reporteRepository = reporteRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
        this.categoriaRepository = categoriaRepository;
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * Vista principal de reportes con todos los tipos disponibles
     */
    @GetMapping("/ver")
    public String verReportes(Model model) {
        try {
            List<Reporte> historial = reporteService.obtenerHistorialReportes();
            model.addAttribute("historial", historial);
            return "Reporte/reportes";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar reportes: " + e.getMessage());
            return "Reporte/reportes";
        }
    }

    // =============== APIs para Dashboard ===============

    /**
     * API: Resumen General (para dashboard)
     */
    @GetMapping("/api/resumen")
    @ResponseBody
    public Map<String, Object> apiResumen() {
        try {
            return reporteService.generarResumenGeneral(1L);
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    /**
     * API: Stock Bajo (para dashboard)
     */
    @GetMapping("/api/stock-bajo")
    @ResponseBody
    public Map<String, Object> apiStockBajo() {
        try {
            return reporteService.generarReporteStockBajo(1L);
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    /**
     * API: Próximos a Vencer (para dashboard)
     */
    @GetMapping("/api/proximos-vencer")
    @ResponseBody
    public Map<String, Object> apiProximosVencer() {
        try {
            return reporteService.generarReporteProximosVencer(1L);
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    // =============== APIs para Vista de Reportes Completa ===============

    /**
     * API: Inventario Completo
     */
    @GetMapping("/api/inventario")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiInventario() {
        try {
            Map<String, Object> datos = reporteService.generarReporteInventario(1L);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * API: Vencidos
     */
    @GetMapping("/api/vencidos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiVencidos() {
        try {
            Map<String, Object> datos = reporteService.generarReporteVencidos(1L);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * API: Sin Movimiento
     */
    @GetMapping("/api/sin-movimiento")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiSinMovimiento() {
        try {
            Map<String, Object> datos = reporteService.generarReporteSinMovimiento(1L);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * API: Movimientos
     */
    @GetMapping("/api/movimientos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiMovimientos() {
        try {
            Map<String, Object> datos = reporteService.generarReporteMovimientos(1L);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * API: Alertas
     */
    @GetMapping("/api/alertas")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apiAlertas() {
        try {
            Map<String, Object> datos = reporteService.generarReporteAlertas(1L);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * API: Historial de Reportes
     */
    @GetMapping("/api/historial")
    @ResponseBody
    public ResponseEntity<List<Reporte>> apiHistorial() {
        try {
            List<Reporte> reportes = reporteService.obtenerHistorialReportes();
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
