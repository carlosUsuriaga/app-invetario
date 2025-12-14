package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadMedidaRepository  extends JpaRepository<UnidadMedida,Long> {
    List<UnidadMedida>findByNombre(String nombre);
    List<UnidadMedida>findByNombreContaining(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByAbreviatura(String abreviatura);

    boolean existsByNombreAndIdNot(String nombre, Long id);
    boolean existsByAbreviaturaAndIdNot(String abreviatura,Long id);

}
