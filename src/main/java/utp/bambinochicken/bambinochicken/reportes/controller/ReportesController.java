package utp.bambinochicken.bambinochicken.reportes.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteKpiResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasItemResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasRequest;
import utp.bambinochicken.bambinochicken.reportes.service.ReportesService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReportesController {

    private final ReportesService reportesService;

    public ReportesController(ReportesService reportesService) {
        this.reportesService = reportesService;
    }

    @PostMapping("/ventas")
    public ApiResponse<List<ReporteVentasItemResponse>> reporteVentas(@Valid @RequestBody ReporteVentasRequest request) {
        return ApiResponse.ok("Reporte de ventas generado", reportesService.reporteVentas(request));
    }

    @GetMapping("/kpis")
    public ApiResponse<List<ReporteKpiResponse>> kpis(@RequestParam(required = false) Long idLocal) {
        return ApiResponse.ok("KPIs obtenidos", reportesService.kpis(idLocal));
    }
}
