package ida.pe.Invetario.repository;

import ida.pe.Invetario.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
