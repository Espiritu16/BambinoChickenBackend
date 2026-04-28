package utp.bambinochicken.bambinochicken.ventas.repository;

import utp.bambinochicken.bambinochicken.ventas.dto.AnularVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.RegistrarVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResponse;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResumenDiarioResponse;

import java.util.List;

public interface VentasRepository {

    List<VentaResponse> findAll();

    VentaResponse save(RegistrarVentaRequest request);

    void anular(Long idVenta, AnularVentaRequest request);

    VentaResumenDiarioResponse resumenDiario(Long idLocal);
}
