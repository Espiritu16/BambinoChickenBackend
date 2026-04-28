package utp.bambinochicken.bambinochicken.inventario.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.inventario.dto.InventarioProductoResponse;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioRequest;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioResponse;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.util.List;

@Repository
public class JdbcInventarioRepository implements InventarioRepository {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public JdbcInventarioRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public List<InventarioProductoResponse> listProductos() {
        throw DomainException.badRequest("Modulo inventario en implementacion (listProductos)");
    }

    @Override
    public List<MovimientoInventarioResponse> listMovimientos() {
        throw DomainException.badRequest("Modulo inventario en implementacion (listMovimientos)");
    }

    @Override
    public MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request) {
        throw DomainException.badRequest("Modulo inventario en implementacion (registrarMovimiento)");
    }
}
