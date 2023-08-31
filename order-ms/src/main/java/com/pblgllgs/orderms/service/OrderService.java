package com.pblgllgs.orderms.service;

import com.pblgllgs.orderms.dto.CustomerOrder;
import com.pblgllgs.orderms.dto.Order;
import com.pblgllgs.orderms.dto.OrderEvent;
import com.pblgllgs.orderms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void createOrder(CustomerOrder customerOrder) {
        Order order = new Order();
        order.setAmount(customerOrder.getAmount());
        order.setItem(customerOrder.getItem());
        order.setQuantity(customerOrder.getQuantity());
        order.setStatus("CREATED");
        try {
            Order orderSaved = orderRepository.save(order);

            customerOrder.setOrderId(orderSaved.getId());

            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrder(customerOrder);
            orderEvent.setType("ORDER-CREATED");
            kafkaTemplate.send("new-orders",orderEvent);
        }catch (Exception e){
            order.setStatus("FAILED");
            orderRepository.save(order);
        }
    }
}
