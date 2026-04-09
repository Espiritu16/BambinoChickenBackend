package utp.bambinochicken.bambinochicken.auth.repository;

import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;

import java.util.Optional;

public interface AuthRepository {
    Optional<AuthSessionEntity> findByAccessToken(String accessToken);

    Optional<AuthSessionEntity> findByRefreshToken(String refreshToken);

    AuthSessionEntity save(AuthSessionEntity session);
}
