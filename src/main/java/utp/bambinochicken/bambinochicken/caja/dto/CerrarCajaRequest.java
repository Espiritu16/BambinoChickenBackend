package utp.bambinochicken.bambinochicken.caja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CerrarCajaRequest(
        @NotNull(message = "montoFinal es obligatorio")
        @PositiveOrZero(message = "montoFinal debe ser mayor o igual a 0")
        Double montoFinal,

        @NotBlank(message = "tipoCierre es obligatorio")
        String tipoCierre
) {
}
