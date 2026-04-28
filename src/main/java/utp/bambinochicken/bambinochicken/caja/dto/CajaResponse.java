package utp.bambinochicken.bambinochicken.caja.dto;

import java.time.OffsetDateTime;

public record CajaResponse(
        Long idCaja,
        Long idLocal,
        Long idUsuario,
        Double montoInicial,
        Double montoFinal,
        String estado,
        String tipoCierre,
        OffsetDateTime fechaApertura,
        OffsetDateTime fechaCierre
) {
}
