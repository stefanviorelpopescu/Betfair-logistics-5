package org.betfair.logistics.controller;

import lombok.RequiredArgsConstructor;
import org.betfair.logistics.dao.dto.OrderDto;
import org.betfair.logistics.exception.CannotCreateResourceException;
import org.betfair.logistics.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/status")
    public List<OrderDto> getOrders(@RequestParam(required = false, defaultValue = "") String date,
                                    @RequestParam(name = "destination", required = false, defaultValue = "") String destinationName) {
        return orderService.findOrders(date, destinationName);
    }

    @PostMapping("/add")
    public List<OrderDto> addOrders(@RequestBody List<OrderDto> orderDtos) throws CannotCreateResourceException {
        return orderService.addOrders(orderDtos);
    }

    @PutMapping("/cancel")
    public void deleteOrders(@RequestBody List<Long> orderIds) {
        orderService.cancelOrders(orderIds);
    }
}
