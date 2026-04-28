package utp.bambinochicken.bambinochicken.proveedores.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROVEEDOR")
public class ProveedorEntity {

    @Id
    @Column(name = "ID_PROVEEDOR", nullable = false)
    private Long idProveedor;

    @Column(name = "NOMBRE", nullable = false, length = 120)
    private String nombre;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "CORREO", length = 120)
    private String correo;

    @Column(name = "DIRECCION", length = 200)
    private String direccion;

    @Column(name = "ESTADO", nullable = false)
    private Integer estado;

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
