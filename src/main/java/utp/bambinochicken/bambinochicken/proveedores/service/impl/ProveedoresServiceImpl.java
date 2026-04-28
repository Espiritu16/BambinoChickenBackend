package utp.bambinochicken.bambinochicken.proveedores.service.impl;

import org.springframework.stereotype.Service;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorResponse;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorUpsertRequest;
import utp.bambinochicken.bambinochicken.proveedores.repository.ProveedoresRepository;
import utp.bambinochicken.bambinochicken.proveedores.service.ProveedoresService;

import java.util.List;

@Service
public class ProveedoresServiceImpl implements ProveedoresService {

    private final ProveedoresRepository proveedoresRepository;

    public ProveedoresServiceImpl(ProveedoresRepository proveedoresRepository) {
        this.proveedoresRepository = proveedoresRepository;
    }

    @Override
    public List<ProveedorResponse> listProveedores(String q) {
        return proveedoresRepository.findAll(q);
    }

    @Override
    public ProveedorResponse createProveedor(ProveedorUpsertRequest request) {
        return proveedoresRepository.save(request);
    }

    @Override
    public ProveedorResponse updateProveedor(Long idProveedor, ProveedorUpsertRequest request) {
        return proveedoresRepository.update(idProveedor, request);
    }

    @Override
    public ProveedorResponse inactivateProveedor(Long idProveedor) {
        return proveedoresRepository.inactivate(idProveedor);
    }
}
