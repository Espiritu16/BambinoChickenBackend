package utp.bambinochicken.bambinochicken.reportes.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReporteVentasRequest(
        @NotNull(message = "fechaInicio es obligatoria")
        LocalDate fechaInicio,
        @NotNull(message = "fechaFin es obligatoria")
        LocalDate fechaFin,
        Long idLocal
) {
}
