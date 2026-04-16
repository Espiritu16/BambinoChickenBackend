package utp.bambinochicken.bambinochicken.auth.persistence.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.auth.persistence.entity.AuthSessionJpaEntity;

import java.util.Optional;

public interface MySqlAuthSessionJpaRepository extends JpaRepository<AuthSessionJpaEntity, Long> {
    Optional<AuthSessionJpaEntity> findByAccessToken(String accessToken);

    Optional<AuthSessionJpaEntity> findByRefreshToken(String refreshToken);
}
