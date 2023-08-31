package com.pblgllgs.orderms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pblgllgs.orderms.dto.Order;
import com.pblgllgs.orderms.dto.OrderEvent;
import com.pblgllgs.orderms.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ReverseOrder {
    private final Logger logger = Logger.getLogger(ReverseOrder.class.getName());
    private final OrderRepository orderRepository;

    @KafkaListener(groupId = "orders-group",topics = "reversed-orders")
    public void reverseOrder(String event) {
        logger.log(Level.WARNING, MessageFormat.format("Reverse order event {0}", event));
        try{
            OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
            Optional<Order> order = orderRepository.findById(orderEvent.getOrder().getOrderId());
            order.ifPresent(o-> {
                o.setStatus("FAILED");
                orderRepository.save(o);
            });

        }catch (Exception e){
            logger.log(Level.WARNING, MessageFormat.format("Something went wrong went try to reverse order {0}", e));
        }
    }
}
