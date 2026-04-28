package utp.bambinochicken.bambinochicken.ventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RegistrarVentaRequest(
        @NotNull(message = "idLocal es obligatorio")
        Long idLocal,

        @NotNull(message = "idCaja es obligatorio")
        Long idCaja,

        @NotBlank(message = "tipoComprobante es obligatorio")
        String tipoComprobante,

        String serieComprobante,
        String numeroComprobante,

        @NotBlank(message = "metodoPago es obligatorio")
        String metodoPago,

        @NotNull(message = "subtotal es obligatorio")
        @PositiveOrZero(message = "subtotal debe ser mayor o igual a 0")
        Double subtotal,

        @NotNull(message = "igv es obligatorio")
        @PositiveOrZero(message = "igv debe ser mayor o igual a 0")
        Double igv,

        @NotNull(message = "total es obligatorio")
        @PositiveOrZero(message = "total debe ser mayor o igual a 0")
        Double total
) {
}
