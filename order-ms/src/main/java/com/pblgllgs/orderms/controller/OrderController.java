package com.pblgllgs.orderms.controller;

import com.pblgllgs.orderms.dto.CustomerOrder;
import com.pblgllgs.orderms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody CustomerOrder customOrder){
        orderService.createOrder(customOrder);
    }
}
