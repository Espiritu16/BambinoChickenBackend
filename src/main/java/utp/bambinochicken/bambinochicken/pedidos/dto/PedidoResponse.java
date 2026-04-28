package utp.bambinochicken.bambinochicken.pedidos.dto;

import java.time.OffsetDateTime;

public record PedidoResponse(
        Long idPedido,
        Long idLocal,
        Long idUsuario,
        Long idMesa,
        String estado,
        String observacion,
        OffsetDateTime fechaPedido
) {
}
