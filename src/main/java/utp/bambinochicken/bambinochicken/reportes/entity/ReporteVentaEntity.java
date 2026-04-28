package utp.bambinochicken.bambinochicken.reportes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "VENTA")
public class ReporteVentaEntity {

    @Id
    @Column(name = "ID_VENTA", nullable = false)
    private Long idVenta;

    @Column(name = "FECHA_VENTA", nullable = false)
    private LocalDateTime fechaVenta;

    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    @Column(name = "ID_LOCAL", nullable = false)
    private Long idLocal;

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }
}
