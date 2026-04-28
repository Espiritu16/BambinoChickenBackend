package utp.bambinochicken.bambinochicken.pedidos.dto;

import jakarta.validation.constraints.NotNull;

public record PedidoUpsertRequest(
        @NotNull(message = "idLocal es obligatorio")
        Long idLocal,
        Long idMesa,
        String observacion
) {
}
