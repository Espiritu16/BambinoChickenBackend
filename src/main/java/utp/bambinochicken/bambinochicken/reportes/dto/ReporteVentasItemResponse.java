package utp.bambinochicken.bambinochicken.reportes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteVentasItemResponse(
        LocalDate fecha,
        Long idVenta,
        String estadoVenta,
        BigDecimal total
) {
}
