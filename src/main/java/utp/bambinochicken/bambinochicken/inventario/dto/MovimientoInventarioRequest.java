package utp.bambinochicken.bambinochicken.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimientoInventarioRequest(
        @NotBlank(message = "tipoMovimiento es obligatorio")
        String tipoMovimiento,

        @NotNull(message = "idProducto es obligatorio")
        Long idProducto,

        @NotNull(message = "cantidad es obligatorio")
        @Positive(message = "cantidad debe ser mayor a 0")
        Integer cantidad,

        String motivo
) {
}
