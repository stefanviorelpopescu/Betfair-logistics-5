package org.betfair.logistics.dao.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.betfair.logistics.dao.OrderStatus;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deliveryDate;

    private Long lastUpdated;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    Destination destination;
}