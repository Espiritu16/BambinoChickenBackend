package utp.bambinochicken.bambinochicken.auth.service;

import utp.bambinochicken.bambinochicken.auth.dto.LoginRequest;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.dto.RefreshTokenRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);
}
