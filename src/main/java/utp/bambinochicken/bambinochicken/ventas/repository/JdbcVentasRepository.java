package utp.bambinochicken.bambinochicken.ventas.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;
import utp.bambinochicken.bambinochicken.ventas.dto.AnularVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.RegistrarVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResponse;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResumenDiarioResponse;

import java.util.List;

@Repository
public class JdbcVentasRepository implements VentasRepository {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public JdbcVentasRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public List<VentaResponse> findAll() {
        throw DomainException.badRequest("Modulo ventas en implementacion (findAll)");
    }

    @Override
    public VentaResponse save(RegistrarVentaRequest request) {
        throw DomainException.badRequest("Modulo ventas en implementacion (save)");
    }

    @Override
    public void anular(Long idVenta, AnularVentaRequest request) {
        throw DomainException.badRequest("Modulo ventas en implementacion (anular)");
    }

    @Override
    public VentaResumenDiarioResponse resumenDiario(Long idLocal) {
        throw DomainException.badRequest("Modulo ventas en implementacion (resumenDiario)");
    }
}
