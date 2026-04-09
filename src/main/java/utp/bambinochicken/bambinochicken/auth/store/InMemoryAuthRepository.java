package utp.bambinochicken.bambinochicken.auth.store;

import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;
import utp.bambinochicken.bambinochicken.auth.repository.AuthRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAuthRepository implements AuthRepository {

    private final ConcurrentHashMap<String, AuthSessionEntity> accessTokenIndex = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AuthSessionEntity> refreshTokenIndex = new ConcurrentHashMap<>();

    @Override
    public Optional<AuthSessionEntity> findByAccessToken(String accessToken) {
        AuthSessionEntity session = accessTokenIndex.get(accessToken);
        if (session == null || session.accessExpiresAt().isBefore(OffsetDateTime.now())) {
            return Optional.empty();
        }
        return Optional.of(session);
    }

    @Override
    public Optional<AuthSessionEntity> findByRefreshToken(String refreshToken) {
        AuthSessionEntity session = refreshTokenIndex.get(refreshToken);
        if (session == null || session.refreshExpiresAt().isBefore(OffsetDateTime.now())) {
            return Optional.empty();
        }
        return Optional.of(session);
    }

    @Override
    public AuthSessionEntity save(AuthSessionEntity session) {
        accessTokenIndex.entrySet().removeIf(e -> e.getValue().userId().equals(session.userId()));
        refreshTokenIndex.entrySet().removeIf(e -> e.getValue().userId().equals(session.userId()));
        accessTokenIndex.put(session.accessToken(), session);
        refreshTokenIndex.put(session.refreshToken(), session);
        return session;
    }
}
