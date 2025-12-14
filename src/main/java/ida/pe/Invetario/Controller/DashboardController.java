package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Producto;
import ida.pe.Invetario.Repository.CategoriaRepository;
import ida.pe.Invetario.Repository.NotificacionRepository;
import ida.pe.Invetario.Repository.ProductoRepository;
import ida.pe.Invetario.Repository.ProveedorRepository;
import ida.pe.Invetario.Service.ReporteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProductoRepository productoRepo;
    private final ProveedorRepository proveedorRepo;
    private final CategoriaRepository categoriaRepo;
    private final NotificacionRepository notificacionRepo;
    private final ReporteService reporteService;

    public DashboardController(ProductoRepository productoRepo, ProveedorRepository proveedorRepo, CategoriaRepository categoriaRepo, NotificacionRepository notificacionRepo, ReporteService reporteService) {
        this.productoRepo = productoRepo;
        this.proveedorRepo = proveedorRepo;
        this.categoriaRepo = categoriaRepo;
        this.notificacionRepo = notificacionRepo;
        this.reporteService = reporteService;
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails userdetails, Model model) {
        try {

            // Datos básicos
            List<Producto> productos = productoRepo.findAll();
            long totalProductos = productos.size();
            long productosActivos = productos.stream().filter(Producto::getEstado).count();
            long productosStockBajo = productos.stream()
                    .filter(p -> p.getStockActual() <= p.getStockMinimo())
                    .count();
            long totalProveedores = proveedorRepo.count();
            long totalCategorias = categoriaRepo.count();
            long notificacionesCount = notificacionRepo.countByLeidoFalse();
            String username=userdetails.getUsername();


            // Agregar al modelo
            model.addAttribute("usuario",username);
            model.addAttribute("totalProductos", totalProductos);
            model.addAttribute("productosActivos", productosActivos);
            model.addAttribute("productosStockBajo", productosStockBajo);
            model.addAttribute("totalProveedores", totalProveedores);
            model.addAttribute("totalCategorias", totalCategorias);
            model.addAttribute("notificacionesCount", notificacionesCount);

            // Usuario (TODO: obtener del contexto de seguridad)
            model.addAttribute("usuarioNombre", "Admin");

            return "Dashboard/dashboard";
        } catch (Exception e) {
            System.err.println("Error en dashboard: " + e.getMessage());
            model.addAttribute("error", "Error al cargar el dashboard");
            return "Dashboard/dashboard";
        }
    }
}
