package ida.pe.Invetario.Model;

import ida.pe.Invetario.enums.TipoReporte;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@EntityListeners(AuditingEntityListener.class)
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReporte tipo;

    @ManyToOne
    @JoinColumn(name = "generado_por", nullable = false)
    private Usuario usuario;

    // Filtros opcionales
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // Estadísticas del reporte
    private Integer totalRegistros = 0;
    private Integer productosActivos = 0;
    private Integer productosInactivos = 0;
    private String resumen;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaActualizacion;

    public Reporte() {
    }

    public Reporte(Long idReporte, String titulo, String descripcion, TipoReporte tipo, Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, Categoria categoria, Integer totalRegistros, Integer productosActivos, Integer productosInactivos, String resumen, LocalDateTime fechaRegistro, LocalDateTime fechaActualizacion) {
        this.idReporte = idReporte;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.usuario = usuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.categoria = categoria;
        this.totalRegistros = totalRegistros;
        this.productosActivos = productosActivos;
        this.productosInactivos = productosInactivos;
        this.resumen = resumen;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoReporte getTipo() {
        return tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public Integer getProductosActivos() {
        return productosActivos;
    }

    public void setProductosActivos(Integer productosActivos) {
        this.productosActivos = productosActivos;
    }

    public Integer getProductosInactivos() {
        return productosInactivos;
    }

    public void setProductosInactivos(Integer productosInactivos) {
        this.productosInactivos = productosInactivos;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
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