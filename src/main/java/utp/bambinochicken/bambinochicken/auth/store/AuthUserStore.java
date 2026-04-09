package utp.bambinochicken.bambinochicken.auth.store;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthUserStore {

    private final Map<String, AuthUserRecord> usersByCorreo = new ConcurrentHashMap<>();

    public AuthUserStore(PasswordEncoder passwordEncoder) {
        usersByCorreo.put("admin@bambino.com", new AuthUserRecord(
                1L,
                "admin@bambino.com",
                passwordEncoder.encode("123456"),
                "ADMINISTRADOR",
                true
        ));
        usersByCorreo.put("cajero@bambino.com", new AuthUserRecord(
                2L,
                "cajero@bambino.com",
                passwordEncoder.encode("123456"),
                "CAJERO",
                true
        ));
        usersByCorreo.put("mozo@bambino.com", new AuthUserRecord(
                3L,
                "mozo@bambino.com",
                passwordEncoder.encode("123456"),
                "MOZO",
                true
        ));
    }

    public Optional<AuthUserRecord> findByCorreo(String correo) {
        return Optional.ofNullable(usersByCorreo.get(correo));
    }
}
