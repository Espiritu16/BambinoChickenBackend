package utp.bambinochicken.bambinochicken.pedidos.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import utp.bambinochicken.bambinochicken.pedidos.dto.*;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.util.List;

@Repository
public class JdbcPedidosRepository implements PedidosRepository {

    private final JdbcTemplate oracleJdbcTemplate;
    private final JdbcTemplate mysqlJdbcTemplate;

    public JdbcPedidosRepository(
            @Qualifier("oracleJdbcTemplate") JdbcTemplate oracleJdbcTemplate,
            @Qualifier("mysqlJdbcTemplate") JdbcTemplate mysqlJdbcTemplate
    ) {
        this.oracleJdbcTemplate = oracleJdbcTemplate;
        this.mysqlJdbcTemplate = mysqlJdbcTemplate;
    }

    @Override
    public List<PedidoResponse> findAll() {
        throw DomainException.badRequest("Modulo pedidos en implementacion (findAll)");
    }

    @Override
    public PedidoResponse save(PedidoUpsertRequest request) {
        throw DomainException.badRequest("Modulo pedidos en implementacion (save)");
    }

    @Override
    public PedidoResponse updateEstado(Long idPedido, ActualizarEstadoPedidoRequest request) {
        throw DomainException.badRequest("Modulo pedidos en implementacion (updateEstado)");
    }

    @Override
    public void anular(Long idPedido, AnularPedidoRequest request) {
        throw DomainException.badRequest("Modulo pedidos en implementacion (anular)");
    }
}
