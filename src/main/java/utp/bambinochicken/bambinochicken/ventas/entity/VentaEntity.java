package utp.bambinochicken.bambinochicken.ventas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "VENTA")
public class VentaEntity {

    @Id
    @Column(name = "ID_VENTA", nullable = false)
    private Long idVenta;

    @Column(name = "FECHA_VENTA", nullable = false)
    private LocalDateTime fechaVenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_COMPROBANTE", nullable = false, length = 20)
    private TipoComprobanteVenta tipoComprobante;

    @Enumerated(EnumType.STRING)
    @Column(name = "METODO_PAGO", nullable = false, length = 30)
    private MetodoPagoVenta metodoPago;

    @Column(name = "SUBTOTAL", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "IGV", nullable = false)
    private BigDecimal igv;

    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
    private EstadoVenta estado;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "ID_CLIENTE")
    private Long idCliente;

    @Column(name = "ID_CAJA")
    private Long idCaja;

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

    public TipoComprobanteVenta getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobanteVenta tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public MetodoPagoVenta getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoVenta metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Long idCaja) {
        this.idCaja = idCaja;
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Long idLocal) {
        this.idLocal = idLocal;
    }
}
