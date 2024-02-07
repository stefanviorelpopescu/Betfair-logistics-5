package org.betfair.logistics.dao.dto;

import lombok.Builder;
import lombok.Data;
import org.betfair.logistics.dao.OrderStatus;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Long deliveryDate;
    private Long lastUpdated;
    private OrderStatus status;
    private Long destinationId;
}
