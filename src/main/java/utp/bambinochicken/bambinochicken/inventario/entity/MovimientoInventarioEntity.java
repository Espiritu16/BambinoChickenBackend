package utp.bambinochicken.bambinochicken.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "MOVIMIENTO_INVENTARIO")
public class MovimientoInventarioEntity {

    @Id
    @Column(name = "ID_MOVIMIENTO", nullable = false)
    private Long idMovimiento;

    @Column(name = "ID_INSUMO", nullable = false)
    private Long idInsumo;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_MOVIMIENTO", nullable = false, length = 20)
    private TipoMovimientoInventario tipoMovimiento;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "STOCK_RESULTANTE", nullable = false)
    private Integer stockResultante;

    @Column(name = "REFERENCIA", length = 60)
    private String referencia;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "FECHA_MOVIMIENTO", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "ID_PRODUCTO")
    private Long idProducto;

    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Long getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Long idInsumo) {
        this.idInsumo = idInsumo;
    }

    public TipoMovimientoInventario getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimientoInventario tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockResultante() {
        return stockResultante;
    }

    public void setStockResultante(Integer stockResultante) {
        this.stockResultante = stockResultante;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }
}
