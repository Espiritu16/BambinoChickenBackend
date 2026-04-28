package utp.bambinochicken.bambinochicken.reportes.dto;

import java.math.BigDecimal;

public record ReporteKpiResponse(
        String nombreKpi,
        BigDecimal valorNumerico,
        String valorTexto
) {
}
