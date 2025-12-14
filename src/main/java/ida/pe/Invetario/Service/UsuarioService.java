package ida.pe.Invetario.Service;

import ida.pe.Invetario.Model.Usuario;
import ida.pe.Invetario.Repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UserRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // Obtener usuario por ID
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Guardar usuario (encriptar contraseña)
    public Usuario guardar(Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // Editar usuario
    public void editar(Long id, Usuario usuarioActualizado) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);

        if (!usuarioExistente.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        Usuario usuario= usuarioExistente.get();

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellido(usuarioActualizado.getApellido());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setUsername(usuarioActualizado.getUsername());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEstado(usuarioActualizado.getEstado());
        if (usuarioActualizado.getPassword() != null &&
                !usuarioActualizado.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }
        usuarioRepository.save(usuario);
    }

    // Anular usuario (cambiar estado)
    public Usuario anular(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setEstado(!usuario.getEstado());
        return usuarioRepository.save(usuario);
    }

    // Validaciones
    public boolean existeUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }


    public boolean existeUsernameExceptoId(String username, Long idUsuario) {
        return usuarioRepository.existsByUsernameAndIdUsuarioNot(username, idUsuario);
    }



}
