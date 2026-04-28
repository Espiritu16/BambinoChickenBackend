package utp.bambinochicken.bambinochicken.pedidos.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.pedidos.entity.PedidoEntity;

public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
}
