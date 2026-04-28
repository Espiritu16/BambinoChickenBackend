package utp.bambinochicken.bambinochicken.proveedores.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorResponse;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorUpsertRequest;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.util.List;

@Repository
public class JdbcProveedoresRepository implements ProveedoresRepository {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public JdbcProveedoresRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public List<ProveedorResponse> findAll(String q) {
        throw DomainException.badRequest("Modulo proveedores en implementacion (findAll)");
    }

    @Override
    public ProveedorResponse save(ProveedorUpsertRequest request) {
        throw DomainException.badRequest("Modulo proveedores en implementacion (save)");
    }

    @Override
    public ProveedorResponse update(Long idProveedor, ProveedorUpsertRequest request) {
        throw DomainException.badRequest("Modulo proveedores en implementacion (update)");
    }

    @Override
    public ProveedorResponse inactivate(Long idProveedor) {
        throw DomainException.badRequest("Modulo proveedores en implementacion (inactivate)");
    }
}
