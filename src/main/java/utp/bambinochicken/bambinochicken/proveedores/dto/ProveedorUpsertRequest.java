package utp.bambinochicken.bambinochicken.proveedores.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProveedorUpsertRequest(
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 120, message = "nombre no debe exceder 120 caracteres")
        String nombre,
        @NotBlank(message = "ruc es obligatorio")
        @Size(min = 11, max = 11, message = "ruc debe tener 11 caracteres")
        String ruc,
        @NotBlank(message = "telefono es obligatorio")
        @Size(max = 20, message = "telefono no debe exceder 20 caracteres")
        String telefono,
        @NotBlank(message = "correo es obligatorio")
        @Size(max = 120, message = "correo no debe exceder 120 caracteres")
        String correo,
        @NotNull(message = "estado es obligatorio")
        Integer estado
) {
}
