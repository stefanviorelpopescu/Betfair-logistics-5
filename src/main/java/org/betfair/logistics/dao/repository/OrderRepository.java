package org.betfair.logistics.dao.repository;

import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByDestination(Destination destination);
    List<Order> findAllByDestination_Id(Long destinationId);

}
