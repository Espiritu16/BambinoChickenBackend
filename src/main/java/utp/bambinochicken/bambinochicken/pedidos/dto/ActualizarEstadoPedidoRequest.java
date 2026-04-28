package utp.bambinochicken.bambinochicken.pedidos.dto;

import jakarta.validation.constraints.NotBlank;

public record ActualizarEstadoPedidoRequest(
        @NotBlank(message = "estado es obligatorio")
        String estado
) {
}
