package utp.bambinochicken.bambinochicken.proveedores.service;

import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorResponse;
import utp.bambinochicken.bambinochicken.proveedores.dto.ProveedorUpsertRequest;

import java.util.List;

public interface ProveedoresService {
    List<ProveedorResponse> listProveedores(String q);

    ProveedorResponse createProveedor(ProveedorUpsertRequest request);

    ProveedorResponse updateProveedor(Long idProveedor, ProveedorUpsertRequest request);

    ProveedorResponse inactivateProveedor(Long idProveedor);
}
