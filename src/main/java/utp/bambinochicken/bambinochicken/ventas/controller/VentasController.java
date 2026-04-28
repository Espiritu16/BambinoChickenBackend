package utp.bambinochicken.bambinochicken.ventas.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;
import utp.bambinochicken.bambinochicken.ventas.dto.AnularVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.RegistrarVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResponse;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResumenDiarioResponse;
import utp.bambinochicken.bambinochicken.ventas.service.VentasService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentasController {

    private final VentasService ventasService;

    public VentasController(VentasService ventasService) {
        this.ventasService = ventasService;
    }

    @GetMapping
    public ApiResponse<List<VentaResponse>> listVentas() {
        return ApiResponse.ok("Ventas obtenidas", ventasService.listVentas());
    }

    @PostMapping
    public ApiResponse<VentaResponse> registrarVenta(@Valid @RequestBody RegistrarVentaRequest request) {
        return ApiResponse.ok("Venta registrada", ventasService.registrarVenta(request));
    }

    @PostMapping("/{idVenta}/anulacion")
    public ApiResponse<Void> anularVenta(
            @PathVariable Long idVenta,
            @Valid @RequestBody AnularVentaRequest request
    ) {
        ventasService.anularVenta(idVenta, request);
        return ApiResponse.ok("Venta anulada", null);
    }

    @GetMapping("/resumen-diario")
    public ApiResponse<VentaResumenDiarioResponse> resumenDiario(@RequestParam(required = false) Long idLocal) {
        return ApiResponse.ok("Resumen diario obtenido", ventasService.resumenDiario(idLocal));
    }
}
