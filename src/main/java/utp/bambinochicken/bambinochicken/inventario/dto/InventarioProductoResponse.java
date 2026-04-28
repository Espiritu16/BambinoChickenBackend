package utp.bambinochicken.bambinochicken.inventario.dto;

public record InventarioProductoResponse(
        Long idProducto,
        String nombre,
        Integer stock,
        Integer stockMinimo,
        String estado
) {
}
