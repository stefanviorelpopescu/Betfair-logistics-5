package org.betfair.logistics.dao.converter;

import org.betfair.logistics.dao.dto.OrderDto;
import org.betfair.logistics.dao.entity.Order;

import java.util.List;

public class OrderConverter {

    public static OrderDto modelToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .deliveryDate(order.getDeliveryDate())
                .lastUpdated(order.getLastUpdated())
                .status(order.getStatus())
                .destinationId(order.getDestination().getId())
                .build();
    }

    public static List<OrderDto> modelListToDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderConverter::modelToDto)
                .toList();
    }

}
