package utp.bambinochicken.bambinochicken.ventas.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.ventas.entity.DetalleVentaEntity;

public interface DetalleVentaJpaRepository extends JpaRepository<DetalleVentaEntity, Long> {
}
