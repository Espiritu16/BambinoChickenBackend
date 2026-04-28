package utp.bambinochicken.bambinochicken.inventario.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.inventario.dto.InventarioProductoResponse;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioRequest;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioResponse;
import utp.bambinochicken.bambinochicken.inventario.service.InventarioService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/productos")
    public ApiResponse<List<InventarioProductoResponse>> listProductos() {
        return ApiResponse.ok("Productos de inventario obtenidos", inventarioService.listProductos());
    }

    @GetMapping("/movimientos")
    public ApiResponse<List<MovimientoInventarioResponse>> listMovimientos() {
        return ApiResponse.ok("Movimientos de inventario obtenidos", inventarioService.listMovimientos());
    }

    @PostMapping("/movimientos")
    public ApiResponse<MovimientoInventarioResponse> registrarMovimiento(
            @Valid @RequestBody MovimientoInventarioRequest request
    ) {
        return ApiResponse.ok("Movimiento de inventario registrado", inventarioService.registrarMovimiento(request));
    }
}
