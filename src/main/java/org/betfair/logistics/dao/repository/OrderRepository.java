package org.betfair.logistics.dao.repository;

import org.betfair.logistics.dao.OrderStatus;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDeliveryDate(Long deliveryDate);
    List<Order> findAllByDestination(Destination destination);
    List<Order> findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(Long deliveryDate, String destinationName);

    default void removeDestinationFromOrder(Order order) {
        order.setDestination(null);
        changeOrderStatus(order, OrderStatus.INVALID);
        save(order);
    }

    default boolean changeOrderStatus(Order order, OrderStatus newStatus) {
        if (OrderStatus.allowedTransitions.get(order.getStatus()).contains(newStatus)) {
            order.setStatus(newStatus);
            return true;
        }
        return false;
    }

    default long changeStatusForOrders(List<Long> orderIds, OrderStatus newStatus) {
        long count = 0;
        List<Order> orderList = findAllById(orderIds);
        for (Order order : orderList) {
            if (changeOrderStatus(order, newStatus)) {
                count ++;
            }
        }
        saveAll(orderList);
        return count;
    }
}
