package utp.bambinochicken.bambinochicken.ventas.service;

import utp.bambinochicken.bambinochicken.ventas.dto.AnularVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.RegistrarVentaRequest;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResponse;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResumenDiarioResponse;

import java.util.List;

public interface VentasService {

    List<VentaResponse> listVentas();

    VentaResponse registrarVenta(RegistrarVentaRequest request);

    void anularVenta(Long idVenta, AnularVentaRequest request);

    VentaResumenDiarioResponse resumenDiario(Long idLocal);
}
