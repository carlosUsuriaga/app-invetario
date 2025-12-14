package ida.pe.Invetario.Model;


import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "Campo nombre vacío, Ingrese un nombre!")
    @Size(min = 3, max = 50)
    private String nombre;


    @NotBlank(message = "Campo apellido vacío!")
    private String apellido;

    @Size(max = 20)
    private String telefono;

    @NotBlank(message = "Ingrese un nombre de usuario!")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Ingrese una contraseña!")
    private String password;

    @NotBlank(message = "Seleccione un rol")
    private String rol;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    private Boolean estado = true;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public Usuario() {
    }

    public Usuario(Long idUsuario, String nombre, String apellido, String telefono, String username, String password, Boolean estado, LocalDateTime fechaRegistro, LocalDateTime fechaActualizacion) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.username = username;
        this.password = password;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
