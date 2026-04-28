package utp.bambinochicken.bambinochicken.pedidos.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.pedidos.dto.*;
import utp.bambinochicken.bambinochicken.pedidos.repository.PedidosRepository;
import utp.bambinochicken.bambinochicken.pedidos.service.PedidosService;

import java.util.List;

@Service
public class PedidosServiceImpl implements PedidosService {

    private final PedidosRepository pedidosRepository;

    public PedidosServiceImpl(PedidosRepository pedidosRepository) {
        this.pedidosRepository = pedidosRepository;
    }

    @Override
    public List<PedidoResponse> listPedidos() {
        return pedidosRepository.findAll();
    }

    @Override
    public PedidoResponse crearPedido(PedidoUpsertRequest request) {
        return pedidosRepository.save(request);
    }

    @Override
    public PedidoResponse actualizarEstado(Long idPedido, ActualizarEstadoPedidoRequest request) {
        return pedidosRepository.updateEstado(idPedido, request);
    }

    @Override
    public void anularPedido(Long idPedido, AnularPedidoRequest request) {
        pedidosRepository.anular(idPedido, request);
    }
}
