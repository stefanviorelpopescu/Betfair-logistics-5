package org.betfair.logistics.service;

import lombok.RequiredArgsConstructor;
import org.betfair.logistics.cache.DestinationCache;
import org.betfair.logistics.config.CompanyInfo;
import org.betfair.logistics.dao.OrderStatus;
import org.betfair.logistics.dao.converter.OrderConverter;
import org.betfair.logistics.dao.dto.OrderDto;
import org.betfair.logistics.dao.entity.Destination;
import org.betfair.logistics.dao.entity.Order;
import org.betfair.logistics.dao.repository.DestinationRepository;
import org.betfair.logistics.dao.repository.OrderRepository;
import org.betfair.logistics.exception.CannotCreateResourceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CompanyInfo companyInfo;
    private final OrderRepository orderRepository;
    private final DestinationCache destinationCache;

    public List<OrderDto> findOrders(String date, String destinationName) {
        Long dateInMillis;
        if (date.isEmpty()) {
            dateInMillis = companyInfo.getCurrentDateAsMillis();
        } else {
            dateInMillis = companyInfo.getMillisFromLocalDate(companyInfo.getLocalDateFromString(date));
        }

        List<Order> foundOrders = orderRepository.findAllByDeliveryDateAndDestination_NameContainingIgnoreCase(dateInMillis, destinationName);
        return OrderConverter.modelListToDtoList(foundOrders);
    }

    public List<OrderDto> addOrders(List<OrderDto> orderDtos) throws CannotCreateResourceException {

        Map<Long, Destination> destinationsById = destinationCache.findAll().stream()
                .collect(Collectors.toMap(Destination::getId, Function.identity()));

        validateOrderDtosForAdd(orderDtos, destinationsById.keySet());

        List<Order> orders = new ArrayList<>();
        for (OrderDto orderDto : orderDtos) {
            orders.add(buildOrderFromDto(orderDto, destinationsById));
        }
        orderRepository.saveAll(orders);

        return OrderConverter.modelListToDtoList(orders);
    }

    public void cancelOrders(List<Long> orderIds) {
        orderRepository.changeStatusForOrders(orderIds, OrderStatus.CANCELED);
    }

    private void validateOrderDtosForAdd(List<OrderDto> orderDtos, Set<Long> existingDestinationIds) throws CannotCreateResourceException {

        StringBuilder errors = new StringBuilder();
        for (int i = 0; i < orderDtos.size(); i++) {
            OrderDto orderDto = orderDtos.get(i);
            if (orderDto.getId() != null) {
                errors.append("Id should not be provided for order with index=").append(i).append("\n");
            }
            if (orderDto.getDeliveryDate() <= companyInfo.getCurrentDateAsMillis()) {
                errors.append("Date must be in the future for order with index=").append(i).append("\n");
            }
            if (!existingDestinationIds.contains(orderDto.getDestinationId())) {
                errors.append("Invalid destination ID for order with index=").append(i).append("\n");
            }
        }
        if (!errors.toString().isEmpty()) {
            throw new CannotCreateResourceException(errors.toString());
        }
    }

    private Order buildOrderFromDto(OrderDto orderDto, Map<Long, Destination> destinationsById) {
        return Order.builder()
                .status(OrderStatus.NEW)
                .lastUpdated(System.currentTimeMillis())
                .deliveryDate(orderDto.getDeliveryDate())
                .destination(destinationsById.get(orderDto.getDestinationId()))
                .build();
    }

}
