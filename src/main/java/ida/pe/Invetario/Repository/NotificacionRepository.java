package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.Notificacion;
import ida.pe.Invetario.enums.TipoNotificacion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion,Integer> {
    List<Notificacion> findByLeidoFalseOrderByFechaRegistroDesc();

    List<Notificacion> findByProductoIdProductoOrderByFechaRegistroDesc(Long idProducto);

    List<Notificacion> findAllByOrderByFechaRegistroDesc();

    Long countByLeidoFalse();
}
