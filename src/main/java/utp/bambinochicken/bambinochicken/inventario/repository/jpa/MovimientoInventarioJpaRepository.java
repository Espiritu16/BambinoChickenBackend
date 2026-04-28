package utp.bambinochicken.bambinochicken.inventario.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.inventario.entity.MovimientoInventarioEntity;

public interface MovimientoInventarioJpaRepository extends JpaRepository<MovimientoInventarioEntity, Long> {
}
