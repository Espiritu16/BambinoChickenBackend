package utp.bambinochicken.bambinochicken.ventas.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.ventas.dto.AnularVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.RegistrarVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResponse;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResumenDiarioResponse;
import utp.bambinochicken.bambinochicken.ventas.repository.VentasRepository;
import utp.bambinochicken.bambinochicken.ventas.service.VentasService;

import java.util.List;

@Service
public class VentasServiceImpl implements VentasService {

    private final VentasRepository ventasRepository;

    public VentasServiceImpl(VentasRepository ventasRepository) {
        this.ventasRepository = ventasRepository;
    }

    @Override
    public List<VentaResponse> listVentas() {
        return ventasRepository.findAll();
    }

    @Override
    public VentaResponse registrarVenta(RegistrarVentaRequest request) {
        return ventasRepository.save(request);
    }

    @Override
    public void anularVenta(Long idVenta, AnularVentaRequest request) {
        ventasRepository.anular(idVenta, request);
    }

    @Override
    public VentaResumenDiarioResponse resumenDiario(Long idLocal) {
        return ventasRepository.resumenDiario(idLocal);
    }
}
