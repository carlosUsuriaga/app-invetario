package ida.pe.Invetario.Config;

import ida.pe.Invetario.Model.Usuario;
import ida.pe.Invetario.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UserRepository usuarioRepo, PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepo.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("User");
                admin.setTelefono("976888918");
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("1234")); // contraseña segura
                admin.setRol("ADMIN");
                admin.setEstado(true);
                usuarioRepo.save(admin);
                System.out.println("Usuario admin creado: admin / 1234");
            }
        };
    }
    @Bean
    CommandLineRunner init1(UserRepository usuarioRepo, PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepo.findByUsername("user").isEmpty()) {
                Usuario user = new Usuario();
                user.setNombre("carlos");
                user.setApellido("daniel");
                user.setTelefono("976888918");
                user.setUsername("user");
                user.setPassword(encoder.encode("1234")); // contraseña segura
                user.setRol("USER");
                user.setEstado(true);
                usuarioRepo.save(user);
                System.out.println("Usuario admin creado: user / 1234");
            }
        };
    }
}
