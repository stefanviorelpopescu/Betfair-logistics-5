package org.betfair.logistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.betfair.logistics.config.CompanyInfo;
import org.betfair.logistics.dao.OrderStatus;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.entity.Order;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShippingService {

    private final CompanyInfo companyInfo;
    private final OrderRepository orderRepository;
    private final ShippingCourier shippingCourier;

    public String advanceDate() {

        Long newDate = companyInfo.advanceDate();
        log.info("New day starting: " + newDate);

        Map<Destination, List<Long>> orderIdsByDestination = orderRepository.findAllByDeliveryDate(newDate)
                .stream()
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .collect(groupingBy(Order::getDestination, mapping(Order::getId, toList())));

        String destinationList = getDestinationNamesJoinedByComma(orderIdsByDestination);
        log.info("Started delivering to " + destinationList);

        for (Map.Entry<Destination, List<Long>> destinationListEntry : orderIdsByDestination.entrySet()) {
            shippingCourier.shipToDestination(destinationListEntry.getKey(), destinationListEntry.getValue());
        }

        return "Started delivering to " + destinationList;
    }

    private static String getDestinationNamesJoinedByComma(Map<Destination, List<Long>> orderIdsByDestination) {
        return orderIdsByDestination.keySet().stream()
                .map(Destination::getName)
                .collect(Collectors.joining(", "));
    }

}
