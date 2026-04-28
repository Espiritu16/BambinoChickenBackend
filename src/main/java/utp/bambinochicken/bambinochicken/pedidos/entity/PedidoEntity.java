package utp.bambinochicken.bambinochicken.pedidos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "PEDIDO")
public class PedidoEntity {

    @Id
    @Column(name = "ID_PEDIDO", nullable = false)
    private Long idPedido;

    @Column(name = "FECHA_PEDIDO", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "NRO_ORDEN", nullable = false)
    private Long nroOrden;

    @Column(name = "NRO_COMANDA", length = 30)
    private String nroComanda;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 30)
    private EstadoPedido estado;

    @Column(name = "OBSERVACION", length = 200)
    private String observacion;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "ID_VENTA")
    private Long idVenta;

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Long getNroOrden() {
        return nroOrden;
    }

    public void setNroOrden(Long nroOrden) {
        this.nroOrden = nroOrden;
    }

    public String getNroComanda() {
        return nroComanda;
    }

    public void setNroComanda(String nroComanda) {
        this.nroComanda = nroComanda;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }
}
