package utp.bambinochicken.bambinochicken.ventas.dto;

public record VentaResumenDiarioResponse(
        String fecha,
        Long idLocal,
        Integer cantidadVentas,
        Double montoTotal
) {
}
