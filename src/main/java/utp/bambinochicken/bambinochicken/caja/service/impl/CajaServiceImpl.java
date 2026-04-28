package utp.bambinochicken.bambinochicken.caja.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.caja.dto.AbrirCajaRequest;
import utp.bambinochicken.bambinochicken.caja.dto.CajaResponse;
import utp.bambinochicken.bambinochicken.caja.dto.CerrarCajaRequest;
import utp.bambinochicken.bambinochicken.caja.repository.CajaRepository;
import utp.bambinochicken.bambinochicken.caja.service.CajaService;
import utp.bambinochicken.bambinochicken.shared.exception.DomainException;

import java.util.List;

@Service
public class CajaServiceImpl implements CajaService {

    private final CajaRepository cajaRepository;

    public CajaServiceImpl(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    @Override
    public List<CajaResponse> listCajas() {
        return cajaRepository.findAll();
    }

    @Override
    public CajaResponse getCajaAbierta(Long idLocal) {
        return cajaRepository.findCajaAbiertaByLocal(idLocal)
                .orElseThrow(() -> DomainException.badRequest("No hay caja abierta para el local solicitado"));
    }

    @Override
    public CajaResponse abrirCaja(AbrirCajaRequest request) {
        return cajaRepository.abrirCaja(request);
    }

    @Override
    public CajaResponse cerrarCaja(Long idCaja, CerrarCajaRequest request) {
        return cajaRepository.cerrarCaja(idCaja, request);
    }
}
