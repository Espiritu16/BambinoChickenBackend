package utp.bambinochicken.bambinochicken.inventario.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.inventario.dto.InventarioProductoResponse;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioRequest;
import utp.bambinochicken.bambinochicken.inventario.dto.MovimientoInventarioResponse;
import utp.bambinochicken.bambinochicken.inventario.repository.InventarioRepository;
import utp.bambinochicken.bambinochicken.inventario.service.InventarioService;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<InventarioProductoResponse> listProductos() {
        return inventarioRepository.listProductos();
    }

    @Override
    public List<MovimientoInventarioResponse> listMovimientos() {
        return inventarioRepository.listMovimientos();
    }

    @Override
    public MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request) {
        return inventarioRepository.registrarMovimiento(request);
    }
}
