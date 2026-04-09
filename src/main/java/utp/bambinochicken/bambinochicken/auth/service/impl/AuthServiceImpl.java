package utp.bambinochicken.bambinochicken.auth.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.auth.dto.LoginRequest;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.dto.RefreshTokenRequest;
import utp.bambinochicken.bambinochicken.auth.service.AuthService;
import utp.bambinochicken.bambinochicken.auth.usecase.LoginUseCase;
import utp.bambinochicken.bambinochicken.auth.usecase.RefreshTokenUseCase;

@Service
public class AuthServiceImpl implements AuthService {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthServiceImpl(LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return loginUseCase.execute(request);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenUseCase.execute(request);
    }
}
