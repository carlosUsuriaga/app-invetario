package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Producto;
import ida.pe.Invetario.Service.CategoriaService;
import ida.pe.Invetario.Service.ProductoService;
import ida.pe.Invetario.Service.ProveedorService;
import ida.pe.Invetario.Service.UnidadMedidaService;
import jakarta.validation.Valid;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final ProveedorService proveedorService;
    private final CategoriaService categoriaService;
    private final UnidadMedidaService unidadMedidaService;
    private final ProductoService productoService;
    public ProductoController(ProveedorService proveedorService, CategoriaService categoriaService,
                              UnidadMedidaService unidadMedidaService, ProductoService productoService) {
        this.proveedorService = proveedorService;
        this.categoriaService = categoriaService;
        this.unidadMedidaService = unidadMedidaService;
        this.productoService = productoService;
    }

    @GetMapping("/agregar")
    public String  mandarObjetoAlFormulario(Model model){
        model.addAttribute("producto",new Producto());
        model.addAttribute("categorias",categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("unidadMedidas",unidadMedidaService.listarUnidaMedida());
        return "Producto/formularioProducto";
    }
    @PostMapping("/agregar")
    public String agregarProducto(@Valid @ModelAttribute Producto producto, BindingResult result,Model model){
        if (productoService.existeNombre(producto.getNombre())){
            result.rejectValue("nombre","error.nombre","El nombre del producto ya existe!");
        }
        if(result.hasErrors()){
            model.addAttribute("categorias",categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("unidadMedidas",unidadMedidaService.listarUnidaMedida());
            return "Producto/formularioProducto";
        }
        productoService.guardarProducto(producto);
        return "redirect:/producto/lista";
    }

    //editar
    @PostMapping("/editar")
    public String editarProducto(@Valid @ModelAttribute Producto producto,BindingResult result,Model model){
        if (productoService.existeNombreExceptoId(producto.getNombre(), producto.getIdProducto())){
            result.rejectValue("nombre","error.nombre","El nombre ya existe!");
        }
        if (result.hasErrors()){
            model.addAttribute("productos",productoService.listarProducto());
            model.addAttribute("categorias",categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("unidadMedidas",unidadMedidaService.listarUnidaMedida());
            model.addAttribute("modalAbierto", true);
            return "Producto/listaProducto";
        }
        productoService.editarProducto(producto.getIdProducto(),producto);
        return "redirect:/producto/lista";
    }
    //anular producto
    @GetMapping("/anular/{id}")
    public String anularProducto(@PathVariable Long id){
        productoService.anualarProducto(id);
        return "redirect:/producto/lista";
    }
    //buscar
    @GetMapping("/buscar")
    @ResponseBody
    public List<Producto> buscarProducto(@RequestParam String nombre){
        return productoService.buscarProductoPorContenido(nombre);
    }
    //listar
    @GetMapping("/lista")
    public String obtenerProductos(Model model){
        model.addAttribute("producto",new Producto());
        model.addAttribute("productos",productoService.listarProducto());
        model.addAttribute("categorias",categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("unidadMedidas",unidadMedidaService.listarUnidaMedida());
        return "Producto/listaProducto";
    }
}
