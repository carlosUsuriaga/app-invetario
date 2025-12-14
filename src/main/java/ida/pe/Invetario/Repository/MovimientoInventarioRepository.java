package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.MovimientoInventario;
import ida.pe.Invetario.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario,Long> {
    // Listar movimientos de un producto específico
    List<MovimientoInventario> findByProducto_IdProducto(Long idProducto);

    // Listar movimientos por tipo
    List<MovimientoInventario> findByTipo(TipoMovimiento tipo);

    // Listar movimientos entre fechas
    List<MovimientoInventario> findByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);

    // Buscar por producto y tipo
    List<MovimientoInventario> findByProducto_IdProductoAndTipo(Long idProducto, TipoMovimiento tipo);

}
