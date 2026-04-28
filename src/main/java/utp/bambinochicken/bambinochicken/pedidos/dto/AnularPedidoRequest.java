package utp.bambinochicken.bambinochicken.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AnularPedidoRequest(
        @NotBlank(message = "motivo es obligatorio")
        @Size(max = 250, message = "motivo maximo 250 caracteres")
        String motivo
) {
}
