package utp.bambinochicken.bambinochicken.auth.dto;

import java.time.OffsetDateTime;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        String rol,
        OffsetDateTime accessTokenExpiresAt
) {
}
