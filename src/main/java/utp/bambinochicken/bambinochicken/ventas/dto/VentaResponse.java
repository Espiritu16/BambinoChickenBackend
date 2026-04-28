package utp.bambinochicken.bambinochicken.ventas.dto;

import java.time.OffsetDateTime;

public record VentaResponse(
        Long idVenta,
        Long idLocal,
        Long idCaja,
        Long idUsuario,
        String tipoComprobante,
        String serieComprobante,
        String numeroComprobante,
        String metodoPago,
        Double subtotal,
        Double igv,
        Double total,
        String estado,
        String motivoAnulacion,
        OffsetDateTime fechaVenta,
        OffsetDateTime fechaAnulacion
) {
}
