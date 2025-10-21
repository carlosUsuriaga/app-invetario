package ida.pe.Invetario.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "unidad_medida")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo nombre vacío, Ingrese una unidad de medida!")
    @Size(min = 1, max = 30, message = "La unidad de medida debe tener entre 1 y 30 caracteres.")
    @Column(nullable = false, unique = true)
    private String nombre; // Ejemplo: kg, litro, unidad

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
}
