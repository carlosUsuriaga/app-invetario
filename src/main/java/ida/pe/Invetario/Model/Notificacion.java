package ida.pe.Invetario.Model;



import ida.pe.Invetario.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notificaciones")
@EntityListeners(AuditingEntityListener.class)
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotificacion;

    @Column(nullable = false, length = 250)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo = TipoNotificacion.ALERTA;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Boolean leido = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
}
