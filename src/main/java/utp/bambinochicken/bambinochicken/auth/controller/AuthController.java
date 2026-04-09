package utp.bambinochicken.bambinochicken.auth.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utp.bambinochicken.bambinochicken.auth.dto.LoginRequest;
import utp.bambinochicken.bambinochicken.auth.dto.LoginResponse;
import utp.bambinochicken.bambinochicken.auth.dto.RefreshTokenRequest;
import utp.bambinochicken.bambinochicken.auth.service.AuthService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("Login executed", authService.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.ok("Token refreshed", authService.refreshToken(request));
    }
}
