package utp.bambinochicken.bambinochicken.auth.dto;

import java.time.OffsetDateTime;

public record LoginResponse(
        Long idUsuario,
        String nombre,
        String correo,
        String rol,
        Long idLocal,
        String localNombre,
        String accessToken,
        String refreshToken,
        String tokenType,
        OffsetDateTime accessTokenExpiresAt
) {
}
