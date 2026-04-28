package utp.bambinochicken.bambinochicken.proveedores.dto;

public record ProveedorResponse(
        Long idProveedor,
        String nombre,
        String ruc,
        String telefono,
        String correo,
        Integer estado
) {
}
