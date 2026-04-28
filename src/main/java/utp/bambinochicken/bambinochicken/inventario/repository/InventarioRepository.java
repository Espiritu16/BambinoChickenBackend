package utp.bambinochicken.bambinochicken.inventario.repository;

import utp.bambinochicken.bambinochicken.inventario.dto.InventarioProductoResponse;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioRequest;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioResponse;

import java.util.List;

public interface InventarioRepository {

    List<InventarioProductoResponse> listProductos();

    List<MovimientoInventarioResponse> listMovimientos();

    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request);
}
