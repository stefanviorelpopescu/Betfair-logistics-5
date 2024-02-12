package org.betfair.logistics.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.betfair.logistics.config.CompanyInfo;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.betfair.logistics.dao.OrderStatus.DELIVERED;
import static org.betfair.logistics.dao.OrderStatus.DELIVERING;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShippingCourier {

    private final OrderRepository orderRepository;
    private final CompanyInfo companyInfo;

    @SneakyThrows
    @Async("executorService")
    public void shipToDestination(Destination destination, List<Long> orderIds) {

        Integer distance = destination.getDistance();
        String destinationName = destination.getName();

        orderRepository.changeStatusForOrders(orderIds, DELIVERING);

        log.info(String.format("Starting deliveries for %s for %d km", destinationName, distance));
        Thread.sleep(distance * 1000);

        long ordersDelivered = orderRepository.changeStatusForOrders(orderIds, DELIVERED);

        companyInfo.increaseProfit(ordersDelivered * distance);
        log.info(String.format("%d deliveries completed for %s", ordersDelivered, destinationName));
    }

}
