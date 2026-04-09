package utp.bambinochicken.bambinochicken.auth.mapper;

import org.springframework.stereotype.Component;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;

@Component
public class AuthMapper {

    public LoginResponse toLoginResponse(AuthSessionEntity session) {
        return new LoginResponse(
                session.accessToken(),
                session.refreshToken(),
                "Bearer",
                session.rol(),
                session.accessExpiresAt()
        );
    }
}
