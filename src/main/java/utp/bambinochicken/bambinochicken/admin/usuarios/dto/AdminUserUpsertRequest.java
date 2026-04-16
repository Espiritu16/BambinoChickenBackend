package utp.bambinochicken.bambinochicken.admin.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminUserUpsertRequest(
        @NotBlank(message = "Nombres es requerido")
        @Size(max = 100, message = "Nombres maximo 100 caracteres")
        String nombres,

        @Size(max = 100, message = "Apellidos maximo 100 caracteres")
        String apellidos,

        @NotBlank(message = "Correo es requerido")
        @Email(message = "Correo invalido")
        @Size(max = 120, message = "Correo maximo 120 caracteres")
        String correo,

        @Size(max = 120, message = "Contrasena maximo 120 caracteres")
        String contrasena,

        @NotNull(message = "Rol es requerido")
        Long idRol
) {
}
