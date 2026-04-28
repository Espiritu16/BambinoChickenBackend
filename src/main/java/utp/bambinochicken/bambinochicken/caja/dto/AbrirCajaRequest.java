package utp.bambinochicken.bambinochicken.caja.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record AbrirCajaRequest(
        @NotNull(message = "idLocal es obligatorio")
        Long idLocal,

        @NotNull(message = "montoInicial es obligatorio")
        @PositiveOrZero(message = "montoInicial debe ser mayor o igual a 0")
        Double montoInicial
) {
}
