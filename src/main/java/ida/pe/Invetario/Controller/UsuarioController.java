package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Usuario;

import ida.pe.Invetario.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/lista")
    public String verLista(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "Usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult result,
                          Model model) {

        // Validar username duplicado al crear
        if (usuario.getIdUsuario() == null && usuarioService.existeUsername(usuario.getUsername())) {
            result.rejectValue("username", "error.username", "El username ya existe!");
        }

        // Validar username duplicado al editar
        if (usuario.getIdUsuario() != null &&
                usuarioService.existeUsernameExceptoId(usuario.getUsername(), usuario.getIdUsuario())) {
            result.rejectValue("username", "error.username", "El username ya existe!");
        }

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarios", usuarioService.listarTodos());
            return "Usuarios/form";
        }

        if (usuario.getIdUsuario() != null) {
            usuarioService.editar(usuario.getIdUsuario(), usuario);
        } else {
            usuarioService.guardar(usuario);
        }

        return "redirect:/usuarios/lista";
    }

    @GetMapping("/anular/{id}")
    public String anular(@PathVariable Long id) {
        usuarioService.anular(id);
        return "redirect:/usuarios/lista";
    }


}
