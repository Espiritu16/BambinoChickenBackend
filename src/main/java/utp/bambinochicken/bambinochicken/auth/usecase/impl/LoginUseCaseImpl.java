package utp.bambinochicken.bambinochicken.auth.usecase.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.auth.dto.LoginRequest;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;
import utp.bambinochicken.bambinochicken.auth.mapper.AuthMapper;
import utp.bambinochicken.bambinochicken.auth.repository.AuthRepository;
import utp.bambinochicken.bambinochicken.auth.store.AuthUserRecord;
import utp.bambinochicken.bambinochicken.auth.store.AuthUserStore;
import utp.bambinochicken.bambinochicken.auth.usecase.LoginUseCase;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private static final String LEGACY_PLACEHOLDER_HASH = "$2a$10$abcdefghijklmnopqrstuv/0123456789ABCDEfghijKLMNO";

    private final AuthMapper authMapper;
    private final AuthUserStore authUserStore;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUseCaseImpl(
            AuthMapper authMapper,
            AuthUserStore authUserStore,
            AuthRepository authRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authMapper = authMapper;
        this.authUserStore = authUserStore;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse execute(LoginRequest request) {
        String correo = request.correo() == null ? "" : request.correo().trim().toLowerCase();
        String contrasena = request.contrasena() == null ? "" : request.contrasena();

        AuthUserRecord user = authUserStore.findByCorreo(correo)
                .orElseThrow(() -> DomainException.unauthorized("Credenciales invalidas"));

        if (!user.activo()) {
            throw DomainException.forbidden("Usuario inactivo");
        }

        if (!passwordMatches(contrasena, user.passwordHash())) {
            throw DomainException.unauthorized("Credenciales invalidas");
        }

        OffsetDateTime issuedAt = OffsetDateTime.now();
        AuthSessionEntity session = new AuthSessionEntity(
                user.id(),
                user.correo(),
                user.rol(),
                "acc_" + UUID.randomUUID(),
                "ref_" + UUID.randomUUID(),
                issuedAt,
                issuedAt.plusHours(8),
                issuedAt.plusDays(7)
        );

        return authMapper.toLoginResponse(authRepository.save(session), user);
    }

    private boolean passwordMatches(String rawPassword, String storedHash) {
        if (storedHash == null || storedHash.isBlank()) {
            return false;
        }

        try {
            if (storedHash.startsWith("$2")) {
                if (passwordEncoder.matches(rawPassword, storedHash)) {
                    return true;
                }
                // Compatibilidad temporal para hashes de demo existentes en BD.
                return LEGACY_PLACEHOLDER_HASH.equals(storedHash) && "123456".equals(rawPassword);
            }
        } catch (IllegalArgumentException ignored) {
            return LEGACY_PLACEHOLDER_HASH.equals(storedHash) && "123456".equals(rawPassword);
        }

        return rawPassword.equals(storedHash);
    }
}
