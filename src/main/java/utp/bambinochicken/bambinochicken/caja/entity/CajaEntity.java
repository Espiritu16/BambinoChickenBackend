package utp.bambinochicken.bambinochicken.caja.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CAJA")
public class CajaEntity {

    @Id
    @Column(name = "ID_CAJA", nullable = false)
    private Long idCaja;

    @Column(name = "FECHA_APERTURA", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "FECHA_CIERRE")
    private LocalDateTime fechaCierre;

    @Column(name = "MONTO_INICIAL", nullable = false)
    private BigDecimal montoInicial;

    @Column(name = "MONTO_FINAL")
    private BigDecimal montoFinal;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoCaja estado;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "ID_LOCAL", nullable = false)
    private Long idLocal;

    public Long getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Long idCaja) {
        this.idCaja = idCaja;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigDecimal getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(BigDecimal montoInicial) {
        this.montoInicial = montoInicial;
    }

    public BigDecimal getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(BigDecimal montoFinal) {
        this.montoFinal = montoFinal;
    }

    public EstadoCaja getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }
}
