package utp.bambinochicken.bambinochicken.auth.store;

public record AuthUserRecord(
        Long id,
        String correo,
        String passwordHash,
        String rol,
        boolean activo
) {
}
