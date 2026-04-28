package utp.bambinochicken.bambinochicken.caja.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.caja.dto.AbrirCajaRequest;
import utp.bambinochicken.bambinochicken.caja.dto.CajaResponse;
import utp.bambinochicken.bambinochicken.caja.dto.CerrarCajaRequest;
import utp.bambinochicken.bambinochicken.caja.service.CajaService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/caja")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @GetMapping
    public ApiResponse<List<CajaResponse>> listCajas() {
        return ApiResponse.ok("Cajas obtenidas", cajaService.listCajas());
    }

    @GetMapping("/abierta")
    public ApiResponse<CajaResponse> getCajaAbierta(@RequestParam Long idLocal) {
        return ApiResponse.ok("Caja abierta obtenida", cajaService.getCajaAbierta(idLocal));
    }

    @PostMapping("/aperturas")
    public ApiResponse<CajaResponse> abrirCaja(@Valid @RequestBody AbrirCajaRequest request) {
        return ApiResponse.ok("Caja abierta correctamente", cajaService.abrirCaja(request));
    }

    @PostMapping("/{idCaja}/cierres")
    public ApiResponse<CajaResponse> cerrarCaja(
            @PathVariable Long idCaja,
            @Valid @RequestBody CerrarCajaRequest request
    ) {
        return ApiResponse.ok("Caja cerrada correctamente", cajaService.cerrarCaja(idCaja, request));
    }
}
