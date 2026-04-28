package utp.bambinochicken.bambinochicken.proveedores.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.proveedores.entity.ProveedorEntity;

public interface ProveedorJpaRepository extends JpaRepository<ProveedorEntity, Long> {
}
