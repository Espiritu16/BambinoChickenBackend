package utp.bambinochicken.bambinochicken.admin.usuarios.dto;

import java.time.OffsetDateTime;

public record AdminUserResponse(
        Long idUsuario,
        String nombres,
        String apellidos,
        String correo,
        Integer estado,
        Long idRol,
        String rol,
        OffsetDateTime creadoEn,
        OffsetDateTime actualizadoEn
) {
}
