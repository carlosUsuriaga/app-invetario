package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.Reporte;
import ida.pe.Invetario.enums.TipoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface ReporteRepository extends JpaRepository<Reporte,Long>{
    /**
     * Obtiene reportes por tipo ordenados por fecha descendente
     */
    List<Reporte> findByTipoOrderByFechaRegistroDesc(TipoReporte tipo);

    /**
     * Obtiene todos los reportes ordenados por fecha
     */
    List<Reporte> findAllByOrderByFechaRegistroDesc();

    /**
     * Obtiene reportes generados por un usuario específico
     */
    List<Reporte> findByUsuario_IdUsuarioOrderByFechaRegistroDesc(Long idUsuario);

    /**
     * Obtiene reportes en un rango de fechas
     */
    List<Reporte> findByFechaRegistroBetweenOrderByFechaRegistroDesc(LocalDate inicio, LocalDate fin);

    /**
     * Obtiene reportes de un tipo específico generados por un usuario
     */
    List<Reporte> findByTipoAndUsuario_IdUsuarioOrderByFechaRegistroDesc(TipoReporte tipo, Long idUsuario);

    /**
     * Cuenta reportes por tipo
     */
    long countByTipo(TipoReporte tipo);
}
