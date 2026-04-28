package utp.bambinochicken.bambinochicken.pedidos.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import utp.bambinochicken.bambinochicken.pedidos.entity.DetallePedidoEntity;

public interface DetallePedidoJpaRepository extends JpaRepository<DetallePedidoEntity, Long> {
}
