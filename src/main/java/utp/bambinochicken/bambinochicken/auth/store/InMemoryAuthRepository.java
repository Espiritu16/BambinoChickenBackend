package utp.bambinochicken.bambinochicken.auth.store;

import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;
import utp.bambinochicken.bambinochicken.auth.persistence.entity.AuthSessionJpaEntity;
import utp.bambinochicken.bambinochicken.auth.persistence.mysql.MySqlAuthSessionJpaRepository;
import utp.bambinochicken.bambinochicken.auth.persistence.oracle.OracleAuthSessionJpaRepository;
import utp.bambinochicken.bambinochicken.auth.repository.AuthRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAuthRepository implements AuthRepository {

    private final ConcurrentHashMap<String, AuthSessionEntity> accessTokenIndex = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AuthSessionEntity> refreshTokenIndex = new ConcurrentHashMap<>();

    private final OracleAuthSessionJpaRepository oracleRepository;
    private final MySqlAuthSessionJpaRepository mySqlRepository;

    public InMemoryAuthRepository(
            OracleAuthSessionJpaRepository oracleRepository,
            MySqlAuthSessionJpaRepository mySqlRepository
    ) {
        this.oracleRepository = oracleRepository;
        this.mySqlRepository = mySqlRepository;
    }

    @Override
    public Optional<AuthSessionEntity> findByAccessToken(String accessToken) {
        AuthSessionEntity cached = accessTokenIndex.get(accessToken);
        if (cached != null && !isExpired(cached.accessExpiresAt())) {
            return Optional.of(cached);
        }

        Optional<AuthSessionEntity> fromOracle = oracleRepository.findByAccessToken(accessToken)
                .map(this::toDomain);
        if (fromOracle.isPresent() && !isExpired(fromOracle.get().accessExpiresAt())) {
            cacheSession(fromOracle.get());
            return fromOracle;
        }

        Optional<AuthSessionEntity> fromMySql = mySqlRepository.findByAccessToken(accessToken)
                .map(this::toDomain);
        if (fromMySql.isPresent() && !isExpired(fromMySql.get().accessExpiresAt())) {
            cacheSession(fromMySql.get());
            return fromMySql;
        }

        return Optional.empty();
    }

    @Override
    public Optional<AuthSessionEntity> findByRefreshToken(String refreshToken) {
        AuthSessionEntity cached = refreshTokenIndex.get(refreshToken);
        if (cached != null && !isExpired(cached.refreshExpiresAt())) {
            return Optional.of(cached);
        }

        Optional<AuthSessionEntity> fromOracle = oracleRepository.findByRefreshToken(refreshToken)
                .map(this::toDomain);
        if (fromOracle.isPresent() && !isExpired(fromOracle.get().refreshExpiresAt())) {
            cacheSession(fromOracle.get());
            return fromOracle;
        }

        Optional<AuthSessionEntity> fromMySql = mySqlRepository.findByRefreshToken(refreshToken)
                .map(this::toDomain);
        if (fromMySql.isPresent() && !isExpired(fromMySql.get().refreshExpiresAt())) {
            cacheSession(fromMySql.get());
            return fromMySql;
        }

        return Optional.empty();
    }

    @Override
    public AuthSessionEntity save(AuthSessionEntity session) {
        removeUserSessionsFromCache(session.userId());
        cacheSession(session);

        AuthSessionJpaEntity entity = toJpaEntity(session);

        // Oracle write first (primary), then MySQL. If MySQL fails, rollback Oracle best-effort.
        oracleRepository.save(entity);
        try {
            mySqlRepository.save(entity);
        } catch (RuntimeException ex) {
            oracleRepository.deleteById(session.userId());
            removeUserSessionsFromCache(session.userId());
            throw ex;
        }

        return session;
    }

    private boolean isExpired(OffsetDateTime expiresAt) {
        return expiresAt.isBefore(OffsetDateTime.now());
    }

    private void cacheSession(AuthSessionEntity session) {
        accessTokenIndex.put(session.accessToken(), session);
        refreshTokenIndex.put(session.refreshToken(), session);
    }

    private void removeUserSessionsFromCache(Long userId) {
        accessTokenIndex.entrySet().removeIf(e -> e.getValue().userId().equals(userId));
        refreshTokenIndex.entrySet().removeIf(e -> e.getValue().userId().equals(userId));
    }

    private AuthSessionJpaEntity toJpaEntity(AuthSessionEntity session) {
        AuthSessionJpaEntity entity = new AuthSessionJpaEntity();
        entity.setUserId(session.userId());
        entity.setCorreo(session.correo());
        entity.setRol(session.rol());
        entity.setAccessToken(session.accessToken());
        entity.setRefreshToken(session.refreshToken());
        entity.setIssuedAt(session.issuedAt().toInstant());
        entity.setAccessExpiresAt(session.accessExpiresAt().toInstant());
        entity.setRefreshExpiresAt(session.refreshExpiresAt().toInstant());
        return entity;
    }

    private AuthSessionEntity toDomain(AuthSessionJpaEntity entity) {
        return new AuthSessionEntity(
                entity.getUserId(),
                entity.getCorreo(),
                entity.getRol(),
                entity.getAccessToken(),
                entity.getRefreshToken(),
                entity.getIssuedAt().atOffset(ZoneOffset.UTC),
                entity.getAccessExpiresAt().atOffset(ZoneOffset.UTC),
                entity.getRefreshExpiresAt().atOffset(ZoneOffset.UTC)
        );
    }
}
