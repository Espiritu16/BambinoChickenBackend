package utp.bambinochicken.bambinochicken.auth.usecase;

import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.dto.RefreshTokenRequest;

public interface RefreshTokenUseCase {
    LoginResponse execute(RefreshTokenRequest request);
}
