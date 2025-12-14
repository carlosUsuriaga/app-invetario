package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.UnidadMedida;
import ida.pe.Invetario.Service.UnidadMedidaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/unidadmedida")
public class UnidadMedidaController {
    private final UnidadMedidaService unidadMedidaService;

    public UnidadMedidaController(UnidadMedidaService unidadMedidaService) {
        this.unidadMedidaService = unidadMedidaService;
    }

    @GetMapping("/agregar")
    public String mandarObjetoAlFormulario(Model model){
        model.addAttribute("unidadmedida",new UnidadMedida());
        return "UnidadMedida/unidadMedidaFormulario";
    }

    @PostMapping("/agregar")
    public String agregarUnidadMedida(@Valid @ModelAttribute("unidadmedida") UnidadMedida unidadmedida, BindingResult result){
        if (unidadMedidaService.existsByNombre(unidadmedida.getNombre())){
            result.rejectValue("nombre","error.nombre","La unidad de medida ya existe!");
        }
        if(unidadMedidaService.existsByAbreviatura(unidadmedida.getAbreviatura())){
            result.rejectValue("abreviatura","error.abreviatura","La abreviatura ya existe");
        }
        if(result.hasErrors()){
            return "UnidadMedida/unidadMedidaFormulario";
        }
        unidadMedidaService.guardarUnidadMedida(unidadmedida);
        return "redirect:/unidadmedida/lista";

    }

    @PostMapping("/editar")
    public String editarUnidaMedida(@Valid @ModelAttribute UnidadMedida unidadMedidaActualizado, BindingResult result, Model model){

        if(unidadMedidaService.existsByNombreAndIdNot(unidadMedidaActualizado.getNombre(),unidadMedidaActualizado.getId())){
            result.rejectValue("nombre","error.nombre","la categoria ya existe");
        }
        if(unidadMedidaService.existsByAbreviaturaAndIdNot(unidadMedidaActualizado.getAbreviatura(),unidadMedidaActualizado.getId())){
            result.rejectValue("abreviatura","error.abreviatura","la abreviatura ya existe");
        }

        if (result.hasErrors()){
            model.addAttribute("listaUM",unidadMedidaService.listarUnidaMedida());
            model.addAttribute("modalAbierto", true);
            return "UnidadMedida/unidadMedidaLista";
        }
        unidadMedidaService.editarUnidaMedida(unidadMedidaActualizado.getId(),unidadMedidaActualizado);
        return "redirect:/unidadmedida/lista";

    }

    @GetMapping("/anular/{id}")
    public String anularUnidaMedida(@PathVariable Long id){
        unidadMedidaService.anularUnidaMedida(id);
        return "redirect:/unidadmedida/lista";

    }

    @GetMapping("/buscar")
    @ResponseBody
    public List<UnidadMedida> buscarUnidaMedida(@RequestParam String nombre){
        return unidadMedidaService.buscarPorContenidoUnidadMedida(nombre);
    }

    @GetMapping("/lista")
    public String listarUnidadMedida(Model model){
        model.addAttribute("listaUM",unidadMedidaService.listarUnidaMedida());
        model.addAttribute("unidadMedida",new UnidadMedida());
        return "UnidadMedida/unidadMedidaLista";
    }








}
