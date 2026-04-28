package utp.bambinochicken.bambinochicken.inventario.dto;

import java.time.OffsetDateTime;

public record MovimientoInventarioResponse(
        Long idMovimientoInventario,
        Long idProducto,
        String tipoMovimiento,
        Integer cantidad,
        String motivo,
        OffsetDateTime fechaMovimiento
) {
}
