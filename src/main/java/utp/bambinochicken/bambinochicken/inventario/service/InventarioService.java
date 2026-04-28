package utp.bambinochicken.bambinochicken.inventario.service;

import utp.bambinochicken.bambinochicken.inventario.dto.InventarioProductoResponse;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioRequest;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioResponse;

import java.util.List;

public interface InventarioService {

    List<InventarioProductoResponse> listProductos();

    List<MovimientoInventarioResponse> listMovimientos();

    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request);
}
