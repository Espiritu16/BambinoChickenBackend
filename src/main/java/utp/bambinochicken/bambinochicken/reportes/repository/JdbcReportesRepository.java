package utp.bambinochicken.bambinochicken.reportes.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteKpiResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasItemResponse;
import utp.bambinochicken.bambinochicken.reportes.dto.ReporteVentasRequest;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.util.List;

@Repository
public class JdbcReportesRepository implements ReportesRepository {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public JdbcReportesRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public List<ReporteVentasItemResponse> reporteVentas(ReporteVentasRequest request) {
        throw DomainException.badRequest("Modulo reportes en implementacion (reporteVentas)");
    }

    @Override
    public List<ReporteKpiResponse> kpis(Long idLocal) {
        throw DomainException.badRequest("Modulo reportes en implementacion (kpis)");
    }
}
