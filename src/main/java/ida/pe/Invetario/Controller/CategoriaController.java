package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Categoria;
import ida.pe.Invetario.Service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    //mostrar formulario y agregar  categoria
    @GetMapping("/agregar")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "Categoria/categoriaFormulario";
    }
    @PostMapping("/agregar")
    public String guardarCategoria(@Valid @ModelAttribute Categoria categoria,BindingResult result) {

        if (categoriaService.existsByNombre(categoria.getNombre())){
            result.rejectValue("nombre","error.nombre","La categoria ya existe!");
        }
        if(result.hasErrors()){
            return "Categoria/categoriaFormulario";
        }

        categoriaService.guardarCategoria(categoria);
        return "redirect:/categoria/lista";
    }

    //editar categoria
    @PostMapping("/editar")
    public String editarCategoria(@Valid @ModelAttribute Categoria categoria, BindingResult result,Model model){
        if (categoriaService.existsByNombreAndIdCategoriaNot(categoria.getNombre(),categoria.getIdCategoria())){
            result.rejectValue("nombre","error.nombre","La categoria ya existe!");
        }
        if (result.hasErrors()){
            model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
            model.addAttribute("modalAbierto", true);
            return "Categoria/categoriaLista";
        }
        categoriaService.EditarCategoria(categoria.getIdCategoria(),categoria);
        return "redirect:/categoria/lista";
    }

    //anular categoria
    @GetMapping("/anular/{id}")
    public String anularCategoria(@PathVariable Long id){
        categoriaService.AnularCategoria(id);
        return "redirect:/categoria/lista";

    }
    //Bucar categoria por nombre
    @GetMapping("/buscar")
    @ResponseBody
    public List<Categoria> BuscarPorNombrequeContenga(@RequestParam String nombre){
        return categoriaService.buscarPorNombreConteniendo(nombre);
    }

    //lista de categorias
    @GetMapping("/lista")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("categoria",new Categoria());
        return "Categoria/categoriaLista";
    }


}
