package utp.bambinochicken.bambinochicken.proveedores.repository;

import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorResponse;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorUpsertRequest;

import java.util.List;

public interface ProveedoresRepository {
    List<ProveedorResponse> findAll(String q);

    ProveedorResponse save(ProveedorUpsertRequest request);

    ProveedorResponse update(Long idProveedor, ProveedorUpsertRequest request);

    ProveedorResponse inactivate(Long idProveedor);
}
