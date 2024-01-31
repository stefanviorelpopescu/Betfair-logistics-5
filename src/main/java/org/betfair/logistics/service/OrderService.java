package org.betfair.logistics.service;

import org.betfair.logistics.dao.OrderStatus;
import org.betfair.logistics.dao.entity.Order;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


}
