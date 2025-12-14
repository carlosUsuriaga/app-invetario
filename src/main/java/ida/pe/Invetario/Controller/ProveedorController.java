package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Proveedor;
import ida.pe.Invetario.Repository.ProveedorRepository;
import ida.pe.Invetario.Service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {


    private final ProveedorService proveedorService;
    public ProveedorController(ProveedorRepository proveedorRepository, ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    //mandar objeto al formulario para registrar
    @GetMapping("/agregar")
    public String mostrarFormulario(Model model){
        model.addAttribute("proveedor",new Proveedor());
        return "Proveedor/formularioProveedor";
    }
    @PostMapping("/agregar")
    public String agregarProveedor(@Valid @ModelAttribute  Proveedor proveedor, BindingResult result){
        if(proveedorService.existsByNombre(proveedor.getNombre())){
            result.rejectValue("nombre","error.nombre","El Nombre ya existe");
        }
        if (proveedorService.validarProveedor(proveedor.getRuc())) {
            result.rejectValue("ruc", "error.ruc", "El RUC ya está registrado");
            return "Proveedor/formularioProveedor";
        }
        if(proveedorService.existsByTelefono(proveedor.getTelefono())){
            result.rejectValue("telefono", "error.telefono", "El Telefono ya está registrado");
        }
        if(proveedorService.existsByCorreo(proveedor.getCorreo())){
            result.rejectValue("correo", "error.correo", "El CORREO ya está registrado");
        }

        if(result.hasErrors()){
            return "Proveedor/formularioProveedor";
        }

        proveedorService.guardarProveedor(proveedor);
        return "redirect:/proveedor/lista";
    }

    //editar proveedor
    @PostMapping("/editar")
    public String editarProveedor(@Valid @ModelAttribute ("proveedor") Proveedor proveedor,BindingResult result,Model model){
        if (proveedorService.existsByNombreAndIdProveedorNot(proveedor.getNombre(), proveedor.getIdProveedor())) {
            result.rejectValue("nombre", "error.nombre", "El nombre ya está registrado");
        }

        if (proveedorService.existsByRucAndIdProveedorNot(proveedor.getRuc(), proveedor.getIdProveedor())) {
            result.rejectValue("ruc", "error.ruc", "El RUC ya está registrado");
        }

        if (proveedorService.existsByTelefonoAndIdProveedorNot(proveedor.getTelefono(), proveedor.getIdProveedor())) {
            result.rejectValue("telefono", "error.telefono", "El teléfono ya está registrado");
        }

        if (proveedorService.existsByCorreoAndIdProveedorNot(proveedor.getCorreo(), proveedor.getIdProveedor())) {
            result.rejectValue("correo", "error.correo", "El correo ya está registrado");
        }
        if (result.hasErrors()){
            model.addAttribute("proveedores",proveedorService.listarProveedor());
            model.addAttribute("modalAbierto", true);
            return "Proveedor/listaProveedor";
        }
        proveedorService.editarProveedor(proveedor.getIdProveedor(),proveedor);
        return "redirect:/proveedor/lista";
    }
    //anular proveedor
    @GetMapping("/anular/{id}")
    public String anularProveedor(@PathVariable Long id){
        proveedorService.anularCategoria(id);
        return "redirect:/proveedor/lista";

    }
    //buscar
    @GetMapping("/buscar")
    @ResponseBody
    public List<Proveedor> buscarProveedorPorContenido(@RequestParam String nombre){
         return proveedorService.buscarPorNombreContenido(nombre);
    }

    @GetMapping("/lista")
    public String listarProveedores(Model model){
         model.addAttribute("proveedores",proveedorService.listarProveedor());
         model.addAttribute("proveedor",new Proveedor());
         return "Proveedor/listaProveedor";
    }


}
