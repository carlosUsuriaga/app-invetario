package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {
    List<Producto> findByNombre(String nombre);
    List<Producto> findByNombreContaining(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdProductoNot(String nombre, Long idProducto);



}
