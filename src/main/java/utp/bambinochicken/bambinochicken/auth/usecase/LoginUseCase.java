package utp.bambinochicken.bambinochicken.auth.usecase;

import utp.bambinochicken.bambinochicken.auth.dto.LoginRequest;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;

public interface LoginUseCase {
    LoginResponse execute(LoginRequest request);
}
