package ida.pe.Invetario.Controller;

import ida.pe.Invetario.Model.MovimientoInventario;
import ida.pe.Invetario.Service.MovimientoInventarioService;
import ida.pe.Invetario.Service.ProductoService;
import ida.pe.Invetario.enums.TipoMovimiento;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movimientos")
public class MovimientoInventarioController {
    private final MovimientoInventarioService movimientoService;
    private final ProductoService productoService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoService,
                                          ProductoService productoService) {
        this.movimientoService = movimientoService;
        this.productoService = productoService;
    }
    @GetMapping("/lista")
    public String verLista(Model model) {
        model.addAttribute("movimiento", new MovimientoInventario());
        model.addAttribute("movimientos", movimientoService.listarTodos());
        model.addAttribute("productos", productoService.listarProducto());
        model.addAttribute("tipos", TipoMovimiento.values());
        return "Movimientos/movimientoLista";
    }

    @PostMapping("/registrar")
    public String registrarMovimiento(@Valid @ModelAttribute("movimiento") MovimientoInventario movimiento,
                                      BindingResult result,
                                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("movimiento", movimiento);
            model.addAttribute("movimientos", movimientoService.listarTodos());
            model.addAttribute("productos", productoService.listarProducto());
            model.addAttribute("tipos", TipoMovimiento.values());
            return "Movimientos/movimientoLista";
        }

        try {
            movimientoService.registrarMovimiento(movimiento);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movimiento", movimiento);
            model.addAttribute("movimientos", movimientoService.listarTodos());
            model.addAttribute("productos", productoService.listarProducto());
            model.addAttribute("tipos", TipoMovimiento.values());
            return "Movimientos/movimientoLista";
        }

        return "redirect:/movimientos/lista";
    }

    @GetMapping("/producto/{id}")
    @ResponseBody
    public List<MovimientoInventario> verMovimientosProducto(@PathVariable Long id) {
        return movimientoService.listarPorProducto(id);
    }
    @GetMapping("/anular/{id}")
    public String anularMovimiento(@PathVariable Long id, Model model) {
        try {
            movimientoService.anularMovimiento(id);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("movimiento", new MovimientoInventario());
            model.addAttribute("movimientos", movimientoService.listarTodos());
            model.addAttribute("productos", productoService.listarProducto());
            model.addAttribute("tipos", TipoMovimiento.values());
            return "Movimientos/movimientoLista";
        }

        return "redirect:/movimientos/lista";
    }



}
