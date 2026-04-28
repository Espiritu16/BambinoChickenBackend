package utp.bambinochicken.bambinochicken.ventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnularVentaRequest(
        @NotBlank(message = "motivo es obligatorio")
        @Size(max = 250, message = "motivo maximo 250 caracteres")
        String motivo
) {
}
