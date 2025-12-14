package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.Categoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria,Long> {
    List<Categoria> findByNombre(String nombre);
    List<Categoria> findByNombreContaining(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdCategoriaNot(String nombre,Long idCategoria);
 }
