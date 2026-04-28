package utp.bambinochicken.bambinochicken.pedidos.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import utp.bambinochicken.bambinochicken.pedidos.dto.*;
import utp.bambinochicken.bambinochicken.pedidos.service.PedidosService;
import utp.bambinochicken.bambinochicken.shared.api.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidosController {

    private final PedidosService pedidosService;

    public PedidosController(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    @GetMapping
    public ApiResponse<List<PedidoResponse>> listPedidos() {
        return ApiResponse.ok("Pedidos obtenidos", pedidosService.listPedidos());
    }

    @PostMapping
    public ApiResponse<PedidoResponse> crearPedido(@Valid @RequestBody PedidoUpsertRequest request) {
        return ApiResponse.ok("Pedido registrado", pedidosService.crearPedido(request));
    }

    @PatchMapping("/{idPedido}/estado")
    public ApiResponse<PedidoResponse> actualizarEstado(
            @PathVariable Long idPedido,
            @Valid @RequestBody ActualizarEstadoPedidoRequest request
    ) {
        return ApiResponse.ok("Estado de pedido actualizado", pedidosService.actualizarEstado(idPedido, request));
    }

    @PostMapping("/{idPedido}/anulacion")
    public ApiResponse<Void> anularPedido(
            @PathVariable Long idPedido,
            @Valid @RequestBody AnularPedidoRequest request
    ) {
        pedidosService.anularPedido(idPedido, request);
        return ApiResponse.ok("Pedido anulado", null);
    }
}
