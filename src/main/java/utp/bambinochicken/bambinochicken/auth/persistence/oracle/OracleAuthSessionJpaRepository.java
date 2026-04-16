package utp.bambinochicken.bambinochicken.auth.persistence.oracle;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.auth.persistence.entity.AuthSessionJpaEntity;

import java.util.Optional;

public interface OracleAuthSessionJpaRepository extends JpaRepository<AuthSessionJpaEntity, Long> {
    Optional<AuthSessionJpaEntity> findByAccessToken(String accessToken);

    Optional<AuthSessionJpaEntity> findByRefreshToken(String refreshToken);
}
