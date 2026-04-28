package utp.bambinochicken.bambinochicken.caja.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.caja.dto.AbrirCajaRequest;
import utp.bambinochicken.bambinochicken.caja.dto.CajaResponse;
import utp.bambinochicken.bambinochicken.caja.dto.CerrarCajaRequest;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCajaRepository implements CajaRepository {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public JdbcCajaRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public List<CajaResponse> findAll() {
        throw DomainException.badRequest("Modulo caja en implementacion (findAll)");
    }

    @Override
    public Optional<CajaResponse> findCajaAbiertaByLocal(Long idLocal) {
        throw DomainException.badRequest("Modulo caja en implementacion (findCajaAbiertaByLocal)");
    }

    @Override
    public CajaResponse abrirCaja(AbrirCajaRequest request) {
        throw DomainException.badRequest("Modulo caja en implementacion (abrirCaja)");
    }

    @Override
    public CajaResponse cerrarCaja(Long idCaja, CerrarCajaRequest request) {
        throw DomainException.badRequest("Modulo caja en implementacion (cerrarCaja)");
    }
}
