package utp.bambinochicken.bambinochicken.auth.entity;

import java.time.OffsetDateTime;

public record AuthSessionEntity(
        Long userId,
        String correo,
        String rol,
        String accessToken,
        String refreshToken,
        OffsetDateTime issuedAt,
        OffsetDateTime accessExpiresAt,
        OffsetDateTime refreshExpiresAt
) {
}
