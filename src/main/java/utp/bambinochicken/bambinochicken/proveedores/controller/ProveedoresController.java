package utp.bambinochicken.bambinochicken.proveedores.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorResponse;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorUpsertRequest;
import utp.bambinochicken.bambinochicken.proveedores.service.ProveedoresService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedoresController {

    private final ProveedoresService proveedoresService;

    public ProveedoresController(ProveedoresService proveedoresService) {
        this.proveedoresService = proveedoresService;
    }

    @GetMapping
    public ApiResponse<List<ProveedorResponse>> listProveedores(@RequestParam(required = false) String q) {
        return ApiResponse.ok("Proveedores obtenidos", proveedoresService.listProveedores(q));
    }

    @PostMapping
    public ApiResponse<ProveedorResponse> createProveedor(@Valid @RequestBody ProveedorUpsertRequest request) {
        return ApiResponse.ok("Proveedor creado", proveedoresService.createProveedor(request));
    }

    @PutMapping("/{idProveedor}")
    public ApiResponse<ProveedorResponse> updateProveedor(
            @PathVariable Long idProveedor,
            @Valid @RequestBody ProveedorUpsertRequest request
    ) {
        return ApiResponse.ok("Proveedor actualizado", proveedoresService.updateProveedor(idProveedor, request));
    }

    @PatchMapping("/{idProveedor}/inactivar")
    public ApiResponse<ProveedorResponse> inactivateProveedor(@PathVariable Long idProveedor) {
        return ApiResponse.ok("Proveedor inactivado", proveedoresService.inactivateProveedor(idProveedor));
    }
}
