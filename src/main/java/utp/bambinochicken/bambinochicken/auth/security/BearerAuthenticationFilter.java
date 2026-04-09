package utp.bambinochicken.bambinochicken.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import utp.bambinochicken.bambinochicken.auth.entity.AuthSessionEntity;
import utp.bambinochicken.bambinochicken.auth.repository.AuthRepository;

import java.io.IOException;
import java.util.List;

@Component
public class BearerAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthRepository authRepository;

    public BearerAuthenticationFilter(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            String accessToken = authorization.substring(BEARER_PREFIX.length());
            authRepository.findByAccessToken(accessToken)
                    .ifPresent(this::authenticate);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(AuthSessionEntity session) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                session.correo(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + session.rol()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
