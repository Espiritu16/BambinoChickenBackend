package utp.bambinochicken.bambinochicken.caja.repository;

import utp.bambinochicken.bambinochicken.caja.dto.AbrirCajaRequest;
import utp.bambinochicken.bambinochicken.caja.dto.CajaResponse;
import utp.bambinochicken.bambinochicken.caja.dto.CerrarCajaRequest;

import java.util.List;
import java.util.Optional;

public interface CajaRepository {

    List<CajaResponse> findAll();

    Optional<CajaResponse> findCajaAbiertaByLocal(Long idLocal);

    CajaResponse abrirCaja(AbrirCajaRequest request);

    CajaResponse cerrarCaja(Long idCaja, CerrarCajaRequest request);
}
