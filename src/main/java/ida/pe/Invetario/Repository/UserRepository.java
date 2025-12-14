package ida.pe.Invetario.Repository;

import ida.pe.Invetario.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdUsuarioNot(String username, Long idUsuario);


}
