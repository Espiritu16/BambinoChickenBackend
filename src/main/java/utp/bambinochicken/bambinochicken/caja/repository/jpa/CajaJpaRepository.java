package utp.bambinochicken.bambinochicken.caja.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.caja.entity.CajaEntity;

public interface CajaJpaRepository extends JpaRepository<CajaEntity, Long> {
}
