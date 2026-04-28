package utp.bambinochicken.bambinochicken.caja.service;

import utp.bambinochicken.bambinochicken.caja.dto.AbrirCajaRequest;
import utp.bambinochicken.bambinochicken.caja.dto.CajaResponse;
import utp.bambinochicken.bambinochicken.caja.dto.CerrarCajaRequest;

import java.util.List;

public interface CajaService {

    List<CajaResponse> listCajas();

    CajaResponse getCajaAbierta(Long idLocal);

    CajaResponse abrirCaja(AbrirCajaRequest request);

    CajaResponse cerrarCaja(Long idCaja, CerrarCajaRequest request);
}
