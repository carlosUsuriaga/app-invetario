package ida.pe.Invetario.Model;

import ida.pe.Invetario.enums.TipoReporte;
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
@Table(name = "reportes")
@EntityListeners(AuditingEntityListener.class)
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReporte;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private TipoReporte tipo = TipoReporte.INVENTARIO;

    @ManyToOne
    @JoinColumn(name = "generado_por")
    private Usuario generadoPor;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
}
