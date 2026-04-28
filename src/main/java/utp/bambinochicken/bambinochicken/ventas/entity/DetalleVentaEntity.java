package utp.bambinochicken.bambinochicken.ventas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_VENTA")
public class DetalleVentaEntity {

    @Id
    @Column(name = "ID_DETALLE_VENTA", nullable = false)
    private Long idDetalleVenta;

    @Column(name = "ID_VENTA", nullable = false)
    private Long idVenta;

    @Column(name = "ID_PRODUCTO", nullable = false)
    private Long idProducto;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "IMPORTE", nullable = false)
    private BigDecimal importe;

    public Long getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(Long idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
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

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}
