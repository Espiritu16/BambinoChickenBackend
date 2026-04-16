package utp.bambinochicken.bambinochicken.auth.mapper;

import org.springframework.stereotype.Component;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;
import utp.bambinochicken.bambinochicken.auth.store.AuthUserRecord;

@Component
public class AuthMapper {

    public LoginResponse toLoginResponse(AuthSessionEntity session, AuthUserRecord user) {
        return new LoginResponse(
                user.id(),
                user.nombre(),
                user.correo(),
                user.rol(),
                user.idLocal(),
                user.localNombre(),
                session.accessToken(),
                session.refreshToken(),
                "Bearer",
                session.accessExpiresAt()
        );
    }
}
