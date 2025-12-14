package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.Notificacion;
import ida.pe.Invetario.Repository.NotificacionRepository;
import ida.pe.Invetario.Service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




// ✅ UN SOLO CONTROLLER CON @Controller (puede devolver vistas y JSON)
@Controller
@RequestMapping("/notificaciones")

public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    // ✅ VER TODAS LAS NOTIFICACIONES
    @GetMapping("/lista")
    public String verLista(Model model) {
        List<Notificacion> notificaciones = notificacionService.obtenerTodas();
        Long noLeidas = notificacionService.contarNoLeidas();

        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("noLeidas", noLeidas);
        model.addAttribute("total", notificaciones.size());

        return "Notificacion/lista";
    }

    // ✅ VER NO LEÍDAS
    @GetMapping("/no-leidas")
    public String verNoLeidas(Model model) {
        List<Notificacion> noLeidas = notificacionService.obtenerNoLeidas();

        model.addAttribute("notificaciones", noLeidas);
        model.addAttribute("noLeidas", noLeidas.size());
        model.addAttribute("total", noLeidas.size());

        return "Notificacion/lista";
    }

    // ✅ VER POR PRODUCTO
    @GetMapping("/producto/{idProducto}")
    public String verPorProducto(@PathVariable Long idProducto, Model model) {
        List<Notificacion> notificaciones = notificacionService.obtenerPorProducto(idProducto);

        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("noLeidas", notificacionService.contarNoLeidas());
        model.addAttribute("total", notificaciones.size());

        return "Notificacion/lista";
    }

    // ✅ MARCAR COMO LEÍDA
    @GetMapping("/marcar-leida/{id}")
    public String marcarComoLeida(@PathVariable Integer id) {
        notificacionService.marcarComoLeida(id);
        return "redirect:/notificaciones/lista";
    }

    // ✅ MARCAR TODAS COMO LEÍDAS
    @GetMapping("/marcar-todas-leidas")
    public String marcarTodasLeidas() {
        notificacionService.marcarTodoComoLeido();
        return "redirect:/notificaciones/lista";
    }

    // ✅ ELIMINAR NOTIFICACIÓN
    @GetMapping("/eliminar/{id}")
    public String eliminarNotificacion(@PathVariable Integer id) {
        notificacionService.eliminarNotificacion(id);
        return "redirect:/notificaciones/lista";
    }
}
