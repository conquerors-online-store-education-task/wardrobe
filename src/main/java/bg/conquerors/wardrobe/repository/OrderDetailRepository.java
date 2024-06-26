package bg.conquerors.wardrobe.repository;

import bg.conquerors.wardrobe.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    void deleteById(Long id);

    OrderDetail findAllById(Long id);

    OrderDetail findAllByOrderIdAndProductId(Long orderId, Long productId);

}
