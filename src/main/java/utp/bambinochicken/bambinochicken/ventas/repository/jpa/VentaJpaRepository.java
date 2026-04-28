package utp.bambinochicken.bambinochicken.ventas.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.ventas.entity.VentaEntity;

public interface VentaJpaRepository extends JpaRepository<VentaEntity, Long> {
}
