package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.Producto;
import ida.pe.Invetario.Repository.ProductoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VencimientoSchedulerService {
    private final ProductoRepository productoRepository;
    private final NotificacionService notificacionService;

    public VencimientoSchedulerService(ProductoRepository productoRepository,
                                       NotificacionService notificacionService) {
        this.productoRepository = productoRepository;
        this.notificacionService = notificacionService;
    }

    // ✅ SE EJECUTA CADA HORA AUTOMÁTICAMENTE
    @Scheduled(fixedDelay = 1800000) //   Cada 60 minutos (3600000 ms)
    public void verificarVencimientos() {
        System.out.println("🔄 Verificando vencimientos de productos...");

        try {
            // Obtener todos los productos activos
            List<Producto> productos = productoRepository.findAll();

            int notificacionesCreadas = 0;

            for (Producto producto : productos) {
                // Verificar próximo vencimiento (2 semanas antes)
                notificacionService.notificarProximoVencimiento(producto);

                // Verificar producto vencido
                notificacionService.notificarProductoVencido(producto);

                notificacionesCreadas++;
            }

            System.out.println("✅ Verificación completada. " + notificacionesCreadas + " productos revisados.");

        } catch (Exception e) {
            System.err.println("❌ Error en scheduler de vencimientos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ✅ MÉTODO MANUAL: Llamar desde el controller cuando quieras
    public void verificarVencimientosManual() {
        System.out.println("🔍 Verificación manual de vencimientos...");

        try {
            List<Producto> productos = productoRepository.findAll();

            for (Producto producto : productos) {
                notificacionService.notificarProximoVencimiento(producto);
                notificacionService.notificarProductoVencido(producto);
            }

            System.out.println("✅ Verificación manual completada.");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
