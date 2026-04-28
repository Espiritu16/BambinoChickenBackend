package utp.bambinochicken.bambinochicken.reportes.repository;

import utp.bambinochicken.bambinochicken.reportes.dto.ReporteKpiResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasItemResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasRequest;

import java.util.List;

public interface ReportesRepository {
    List<ReporteVentasItemResponse> reporteVentas(ReporteVentasRequest request);

    List<ReporteKpiResponse> kpis(Long idLocal);
}
