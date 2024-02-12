package org.betfair.logistics.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderStatus {
    NEW,
    DELIVERING,
    DELIVERED,
    CANCELED,
    INVALID,
    ;
    public static final Map<OrderStatus, List<OrderStatus>> allowedTransitions = new HashMap<>();

    static {
        allowedTransitions.put(NEW, List.of(DELIVERING, CANCELED, INVALID));
        allowedTransitions.put(DELIVERING, List.of(DELIVERED, CANCELED, INVALID));
        allowedTransitions.put(DELIVERED, List.of(INVALID));
        allowedTransitions.put(CANCELED, List.of(NEW, INVALID));
        allowedTransitions.put(INVALID, Collections.emptyList());
    }
}
