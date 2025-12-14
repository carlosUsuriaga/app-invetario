package ida.pe.Invetario.Config;

import ida.pe.Invetario.Service.CustomUserDetailsService;
import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class Config {


    private final CustomUserDetailsService userDetailsService;



    public Config(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (sin autenticación)
                        .requestMatchers("/login", "/css/**", "/js/**", "/Login/**").permitAll()
                        .requestMatchers("/Notificacion/**","/Dashboard/**","/Reporte/**").permitAll()
                        // Rutas solo para ADMIN
                        .requestMatchers("/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/roles/**").hasRole("ADMIN")
                        .    requestMatchers("/reporte/**").hasRole("ADMIN")
                        .requestMatchers("/movimiento/**").hasRole("ADMIN")

                        // Rutas para ADMIN y USER
                        .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/producto/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/categoria/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/proveedor/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/unidadmedida/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/notificaciones/**").hasAnyRole("ADMIN", "USER")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                // Para manejar acceso denegado
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }




}
