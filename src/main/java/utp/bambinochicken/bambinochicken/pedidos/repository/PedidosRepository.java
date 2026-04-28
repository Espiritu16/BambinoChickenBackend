package utp.bambinochicken.bambinochicken.pedidos.repository;

import utp.bambinochicken.bambinochicken.pedidos.dto.*;

import java.util.List;

public interface PedidosRepository {

    List<PedidoResponse> findAll();

    PedidoResponse save(PedidoUpsertRequest request);

    PedidoResponse updateEstado(Long idPedido, ActualizarEstadoPedidoRequest request);

    void anular(Long idPedido, AnularPedidoRequest request);
}
