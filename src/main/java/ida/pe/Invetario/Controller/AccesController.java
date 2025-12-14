package ida.pe.Invetario.Controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccesController {
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "Usuarios/acceso-denegado";
    }
}
