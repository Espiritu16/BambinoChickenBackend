package utp.bambinochicken.bambinochicken.admin.usuarios.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.admin.usuarios.dto.AdminRoleResponse;
import utp.bambinochicken.bambinochicken.admin.usuarios.dto.AdminUserResponse;
import utp.bambinochicken.bambinochicken.admin.usuarios.dto.AdminUserUpsertRequest;
import utp.bambinochicken.bambinochicken.admin.usuarios.service.AdminUserService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/usuarios")
    public ApiResponse<List<AdminUserResponse>> searchUsers(@RequestParam(required = false) String q) {
        return ApiResponse.ok("Usuarios obtenidos", adminUserService.searchUsers(q));
    }

    @GetMapping("/roles")
    public ApiResponse<List<AdminRoleResponse>> listRoles() {
        return ApiResponse.ok("Roles obtenidos", adminUserService.listRoles());
    }

    @PostMapping("/usuarios")
    public ApiResponse<AdminUserResponse> createUser(@Valid @RequestBody AdminUserUpsertRequest request) {
        return ApiResponse.ok("Usuario creado", adminUserService.createUser(request));
    }

    @PutMapping("/usuarios/{idUsuario}")
    public ApiResponse<AdminUserResponse> updateUser(
            @PathVariable Long idUsuario,
            @Valid @RequestBody AdminUserUpsertRequest request
    ) {
        return ApiResponse.ok("Usuario actualizado", adminUserService.updateUser(idUsuario, request));
    }

    @PatchMapping("/usuarios/{idUsuario}/inactivar")
    public ApiResponse<AdminUserResponse> inactivateUser(@PathVariable Long idUsuario) {
        return ApiResponse.ok("Usuario inactivado", adminUserService.inactivateUser(idUsuario));
    }
}
