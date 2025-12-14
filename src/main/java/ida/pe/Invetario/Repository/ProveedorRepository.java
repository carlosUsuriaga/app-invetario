package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor,Long> {
    List<Proveedor> findByNombre(String nombre);
    List<Proveedor> findByNombreContaining(String nombre);

    boolean existsByNombre(String nombre);
    boolean existsByRuc(String ruc);
    boolean existsByTelefono(String telefono);
    boolean existsByCorreo(String correo);


    boolean existsByNombreAndIdProveedorNot(String nombre, Long idProveedor);
    boolean existsByRucAndIdProveedorNot(String ruc,Long idProveedor);
    boolean existsByTelefonoAndIdProveedorNot(String telefono,Long idProveedor);
    boolean existsByCorreoAndIdProveedorNot(String correo,Long idProveedor);

}
