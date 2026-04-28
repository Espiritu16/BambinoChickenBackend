package utp.bambinochicken.bambinochicken.pedidos.service;

import utp.bambinochicken.bambinochicken.pedidos.dto.*;

import java.util.List;

public interface PedidosService {

    List<PedidoResponse> listPedidos();

    PedidoResponse crearPedido(PedidoUpsertRequest request);

    PedidoResponse actualizarEstado(Long idPedido, ActualizarEstadoPedidoRequest request);

    void anularPedido(Long idPedido, AnularPedidoRequest request);
}
