package utp.bambinochicken.bambinochicken.pedidos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DETALLE_PEDIDO")
public class DetallePedidoEntity {

    @Id
    @Column(name = "ID_DETALLE_PEDIDO", nullable = false)
    private Long idDetallePedido;

    @Column(name = "ID_PEDIDO", nullable = false)
    private Long idPedido;

    @Column(name = "ID_PRODUCTO", nullable = false)
    private Long idProducto;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "OBSERVACION", length = 200)
    private String observacion;

    public Long getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(Long idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
