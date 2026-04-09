package utp.bambinochicken.bambinochicken.auth.usecase.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.dto.RefreshTokenRequest;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;
import utp.bambinochicken.bambinochicken.auth.mapper.AuthMapper;
import utp.bambinochicken.bambinochicken.auth.repository.AuthRepository;
import utp.bambinochicken.bambinochicken.auth.usecase.RefreshTokenUseCase;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final AuthMapper authMapper;
    private final AuthRepository authRepository;

    public RefreshTokenUseCaseImpl(AuthMapper authMapper, AuthRepository authRepository) {
        this.authMapper = authMapper;
        this.authRepository = authRepository;
    }

    @Override
    public LoginResponse execute(RefreshTokenRequest request) {
        AuthSessionEntity currentSession = authRepository.findByRefreshToken(request.refreshToken())
                .orElseThrow(() -> DomainException.unauthorized("Refresh token invalido o expirado"));

        OffsetDateTime issuedAt = OffsetDateTime.now();
        AuthSessionEntity session = new AuthSessionEntity(
                currentSession.userId(),
                currentSession.correo(),
                currentSession.rol(),
                "acc_" + UUID.randomUUID(),
                currentSession.refreshToken(),
                issuedAt,
                issuedAt.plusHours(8),
                currentSession.refreshExpiresAt()
        );
        return authMapper.toLoginResponse(authRepository.save(session));
    }
}
