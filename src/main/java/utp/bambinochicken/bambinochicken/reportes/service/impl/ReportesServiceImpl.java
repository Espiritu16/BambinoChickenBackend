package utp.bambinochicken.bambinochicken.reportes.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteKpiResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasItemResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasRequest;
import utp.bambinochicken.bambinochicken.reportes.repository.ReportesRepository;
import utp.bambinochicken.bambinochicken.reportes.service.ReportesService;

import java.util.List;

@Service
public class ReportesServiceImpl implements ReportesService {

    private final ReportesRepository reportesRepository;

    public ReportesServiceImpl(ReportesRepository reportesRepository) {
        this.reportesRepository = reportesRepository;
    }

    @Override
    public List<ReporteVentasItemResponse> reporteVentas(ReporteVentasRequest request) {
        return reportesRepository.reporteVentas(request);
    }

    @Override
    public List<ReporteKpiResponse> kpis(Long idLocal) {
        return reportesRepository.kpis(idLocal);
    }
}
